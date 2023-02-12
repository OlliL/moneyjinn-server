
package org.laladev.moneyjinn.server.controller.comparedata;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.builder.openapi.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.openapi.CompareDataDatasetTransportBuilder;
import org.laladev.moneyjinn.server.builder.openapi.CompareDataFormatTransportBuilder;
import org.laladev.moneyjinn.server.builder.openapi.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.openapi.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.CompareDataMatchingTransport;
import org.laladev.moneyjinn.server.model.CompareDataNotInDatabaseTransport;
import org.laladev.moneyjinn.server.model.CompareDataNotInFileTransport;
import org.laladev.moneyjinn.server.model.CompareDataRequest;
import org.laladev.moneyjinn.server.model.CompareDataResponse;
import org.laladev.moneyjinn.server.model.CompareDataWrongCapitalsourceTransport;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.FileCopyUtils;

public class CompareDataTest extends AbstractControllerTest {
  private final HttpMethod method = HttpMethod.PUT;
  private String userName;
  private String userPassword;
  @Value("classpath:comparedata/postbank_online.csv")
  private Resource postbankOnlineResource;
  @Value("classpath:comparedata/sparda_bank.csv")
  private Resource spardaBankResource;
  @Value("classpath:comparedata/sparkasse.csv")
  private Resource sparkasseResource;
  @Value("classpath:comparedata/volksbank.csv")
  private Resource volksbankResource;
  @Value("classpath:comparedata/camt.xml")
  private Resource camtResource;

  @BeforeEach
  public void setUp() {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
  }

  @Override
  protected String getUsername() {
    return this.userName;
  }

  @Override
  protected String getPassword() {
    return this.userPassword;
  }

  @Override
  protected String getUsecase() {
    return super.getUsecaseFromTestClassName(this.getClass());
  }

  @Test
  public void test_bookedWithWrongCapitalsource_Successfull() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    request.setStartDate(LocalDate.parse("2010-05-01"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    final String base64FileContents = this.getFileContents(this.postbankOnlineResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();
    final CompareDataWrongCapitalsourceTransport compareDataWrongCapitalsourceTransport = new CompareDataWrongCapitalsourceTransport();
    compareDataWrongCapitalsourceTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow19().build());
    compareDataWrongCapitalsourceTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset1().build());
    expected.setCompareDataWrongCapitalsourceTransports(
        Collections.singletonList(compareDataWrongCapitalsourceTransport));
    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset2().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_privateFlowProcessedButNotShown_bookedWithWrongCapitalsource_Successfull()
      throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    request.setStartDate(LocalDate.parse("2010-05-03"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    final String base64FileContents = this.getFileContents(this.postbankOnlineResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();
    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset2().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_privateFlowProcessedButNotShown_notInFile_Successfull() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2009-02-01"));
    request.setEndDate(LocalDate.parse("2009-02-20"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    final String base64FileContents = this.getFileContents(this.postbankOnlineResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_privateFlowProcessedButNotShown_matching_Successfull() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-05-03"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    final String base64FileContents = this.getFileContents(this.postbankOnlineResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset2().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_noDataForThisMonth_FileContentIsFilteredAllMoneyflowsMissing() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-03-01"));
    request.setEndDate(LocalDate.parse("2010-03-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    final String base64FileContents = this.getFileContents(this.postbankOnlineResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
    compareDataNotInFileTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow16().build());
    expected.setCompareDataNotInFileTransports(
        Collections.singletonList(compareDataNotInFileTransport));

    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_onePossibleMatch_matchIsTakenNoRatingInvolved() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-02-01"));
    request.setEndDate(LocalDate.parse("2010-02-28"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    final String base64FileContents = this.getFileContents(this.postbankOnlineResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    final CompareDataMatchingTransport compareDataMatchingTransport = new CompareDataMatchingTransport();
    compareDataMatchingTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow15().build());
    compareDataMatchingTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset3().build());
    expected
        .setCompareDataMatchingTransports(Collections.singletonList(compareDataMatchingTransport));

    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_moneyflowIsOutOfSearchFrame_missingReported() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-01-01"));
    request.setEndDate(LocalDate.parse("2010-01-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    final String base64FileContents = this.getFileContents(this.postbankOnlineResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
    compareDataNotInFileTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow14().build());
    expected.setCompareDataNotInFileTransports(
        Collections.singletonList(compareDataNotInFileTransport));
    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset4().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_invalidFileFormat_Exception() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-01-01"));
    request.setEndDate(LocalDate.parse("2010-01-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    // just pick a file which does not match the above set FormatId
    final String base64FileContents = this.getFileContents(this.spardaBankResource);
    request.setFileContents(base64FileContents);
    final ErrorResponse expected = new ErrorResponse();
    expected.setMessage(
        "The specified file is not parseable! Maybe you've selected the wrong format or file?");
    expected.setCode(ErrorCode.WRONG_FILE_FORMAT.getErrorCode());
    final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ErrorResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_standardRequest_SpardaBank_Successfull() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-05-01"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT2_ID);
    final String base64FileContents = this.getFileContents(this.spardaBankResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    // No Contractpartner information in this CSV, so the bookingdate equality wins
    final CompareDataMatchingTransport compareDataMatchingTransport = new CompareDataMatchingTransport();
    compareDataMatchingTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow18().build());
    compareDataMatchingTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset1SpardaBank().build());
    expected
        .setCompareDataMatchingTransports(Collections.singletonList(compareDataMatchingTransport));
    final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
    compareDataNotInFileTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow19().build());
    expected.setCompareDataNotInFileTransports(
        Collections.singletonList(compareDataNotInFileTransport));
    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset2SpardaBank().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_standardRequest_PostbankOnline_Successfull() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-05-01"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT3_ID);
    final String base64FileContents = this.getFileContents(this.postbankOnlineResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    // there is a booking with the same amount on the same date as in the file, but the 100%
    // matching Contractpartner overrules this so the other moneyflow is picked.
    final CompareDataMatchingTransport compareDataMatchingTransport = new CompareDataMatchingTransport();
    compareDataMatchingTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow19().build());
    compareDataMatchingTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset1().build());
    expected
        .setCompareDataMatchingTransports(Collections.singletonList(compareDataMatchingTransport));
    final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
    compareDataNotInFileTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow18().build());
    expected.setCompareDataNotInFileTransports(
        Collections.singletonList(compareDataNotInFileTransport));
    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset2().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_standardRequest_Sparkasse_Successfull() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-05-01"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT5_ID);
    final String base64FileContents = this.getFileContents(this.sparkasseResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    // there is a booking with the same amount on the same date as in the file, but the 100%
    // matching Contractpartner overrules this so the other moneyflow is picked.
    final CompareDataMatchingTransport compareDataMatchingTransport = new CompareDataMatchingTransport();
    compareDataMatchingTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow19().build());
    compareDataMatchingTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset1().build());
    expected
        .setCompareDataMatchingTransports(Collections.singletonList(compareDataMatchingTransport));
    final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
    compareDataNotInFileTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow18().build());
    expected.setCompareDataNotInFileTransports(
        Collections.singletonList(compareDataNotInFileTransport));
    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset2().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_standardRequest_Volksbank_Successfull() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-05-01"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT6_ID);
    final String base64FileContents = this.getFileContents(this.volksbankResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    // there is a booking with the same amount on the same date as in the file, but the Soundex
    // matching Contractpartner overrules this so the other moneyflow is picked.
    final CompareDataMatchingTransport compareDataMatchingTransport = new CompareDataMatchingTransport();
    compareDataMatchingTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow19().build());
    compareDataMatchingTransport
        .setCompareDataDatasetTransport(new CompareDataDatasetTransportBuilder()
            .forCompareDataDataset1().withPartner("Qartnär2").build());
    expected
        .setCompareDataMatchingTransports(Collections.singletonList(compareDataMatchingTransport));
    final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
    compareDataNotInFileTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow18().build());
    expected.setCompareDataNotInFileTransports(
        Collections.singletonList(compareDataNotInFileTransport));
    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset2().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_standardRequest_Camt_Successfull() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-05-01"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setFormatId(CompareDataFormatTransportBuilder.COMPARE_DATA_FORMAT4_ID);
    final String base64FileContents = this.getFileContents(this.camtResource);
    request.setFileContents(base64FileContents);
    final CompareDataResponse expected = new CompareDataResponse();

    // there is a booking with the same amount on the same date as in the file, but the 100%
    // matching Contractpartner overrules this so the other moneyflow is picked.
    final CompareDataMatchingTransport compareDataMatchingTransport = new CompareDataMatchingTransport();
    compareDataMatchingTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow19().build());
    compareDataMatchingTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset1().build());
    expected
        .setCompareDataMatchingTransports(Collections.singletonList(compareDataMatchingTransport));
    final CompareDataNotInFileTransport compareDataNotInFileTransport = new CompareDataNotInFileTransport();
    compareDataNotInFileTransport
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow18().build());
    expected.setCompareDataNotInFileTransports(
        Collections.singletonList(compareDataNotInFileTransport));
    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataDataset2().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_standardRequest_Import_Successfull() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    request.setStartDate(LocalDate.parse("2010-05-01"));
    request.setEndDate(LocalDate.parse("2010-05-31"));
    request.setUseImportedData(1);
    final CompareDataResponse expected = new CompareDataResponse();

    // there is a booking with the same amount on the same date as in the file, but the 100%
    // matching Contractpartner overrules this so the other moneyflow is picked.
    final CompareDataMatchingTransport compareDataMatchingTransport1 = new CompareDataMatchingTransport();
    compareDataMatchingTransport1
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow18().build());
    compareDataMatchingTransport1.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataImportDataset1().build());
    final CompareDataMatchingTransport compareDataMatchingTransport2 = new CompareDataMatchingTransport();
    compareDataMatchingTransport2
        .setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow19().build());
    compareDataMatchingTransport2.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataImportDataset3().build());

    expected.setCompareDataMatchingTransports(
        Arrays.asList(compareDataMatchingTransport1, compareDataMatchingTransport2));

    final CompareDataNotInDatabaseTransport compareDataNotInDatabaseTransport = new CompareDataNotInDatabaseTransport();
    compareDataNotInDatabaseTransport.setCompareDataDatasetTransport(
        new CompareDataDatasetTransportBuilder().forCompareDataImportDataset2().build());
    expected.setCompareDataNotInDatabaseTransports(
        Collections.singletonList(compareDataNotInDatabaseTransport));
    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_accessesImportedMoneyflowsBeforeGroupAssignemnts_dataNotShown()
      throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    request.setStartDate(LocalDate.parse("1999-01-01"));
    request.setEndDate(LocalDate.parse("1999-01-10"));
    request.setUseImportedData(1);
    final CompareDataResponse expected = new CompareDataResponse();

    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_accessesImportedMoneyflowsAfterGroupAssignemnts_dataNotShown() throws Exception {
    final CompareDataRequest request = new CompareDataRequest();
    request.setCapitalsourceId(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    request.setStartDate(LocalDate.parse("2600-01-01"));
    request.setEndDate(LocalDate.parse("2600-01-10"));
    request.setUseImportedData(1);
    final CompareDataResponse expected = new CompareDataResponse();

    final CompareDataResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        CompareDataResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  private String getFileContents(final Resource resource) throws IOException {
    final byte[] rawFileContents = FileCopyUtils.copyToByteArray(resource.getInputStream());
    final UniversalDetector detector = new UniversalDetector(null);
    detector.handleData(rawFileContents, 0, rawFileContents.length);
    detector.dataEnd();
    String encoding = detector.getDetectedCharset();
    detector.reset();
    if (encoding == null) {
      encoding = "UTF-8";
    }
    final String stringFileContents = new String(rawFileContents, encoding);
    // final String stringFileContents = new String(rawFileContents,
    // StandardCharsets.ISO_8859_1);
    final String base64FileContents = Base64.getMimeEncoder()
        .encodeToString(stringFileContents.getBytes());
    return base64FileContents;
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final CompareDataRequest request = new CompareDataRequest();
    super.callUsecaseWithContent("", this.method, request, false, CompareDataResponse.class);
  }
}