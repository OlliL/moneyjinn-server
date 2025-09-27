package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.ImportedMoneyflowReceiptTransport;

public class ImportedMoneyflowReceiptTransportBuilder extends ImportedMoneyflowReceiptTransport {
    public static final Long RECEIPT_1ID = 1L;
    public static final Long RECEIPT_2ID = 2L;
    public static final Long NEXT_ID = 3L;
    private static final String JPEG_FILE = "/9j/4AAQSkZJRgABAQEBLAEsAAD//gATQ3JlYXRlZCB3aXRoIEdJ"
            + "TVD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGB"
            + "YUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQU"
            + "FBQUFBQUFBQUFBT/wgARCAABAAEDAREAAhEBAxEB/8QAFAABAAAAAAAAAAAAAAAAAAAACP/EABQBAQAAAAAAAA"
            + "AAAAAAAAAAAAD/2gAMAwEAAhADEAAAAVSf/8QAFBABAAAAAAAAAAAAAAAAAAAAAP/aAAgBAQABBQJ//8QAFBEB"
            + "AAAAAAAAAAAAAAAAAAAAAP/aAAgBAwEBPwF//8QAFBEBAAAAAAAAAAAAAAAAAAAAAP/aAAgBAgEBPwF//8QAFB"
            + "ABAAAAAAAAAAAAAAAAAAAAAP/aAAgBAQAGPwJ//8QAFBABAAAAAAAAAAAAAAAAAAAAAP/aAAgBAQABPyF//9oA"
            + "DAMBAAIAAwAAABCf/8QAFBEBAAAAAAAAAAAAAAAAAAAAAP/aAAgBAwEBPxB//8QAFBEBAAAAAAAAAAAAAAAAAA"
            + "AAAP/aAAgBAgEBPxB//8QAFBABAAAAAAAAAAAAAAAAAAAAAP/aAAgBAQABPxB//9k=";
    private static final String PDF_FILE = "JVBERi0xLjUKJbXtrvsKNCAwIG9iago8PCAvTGVuZ3RoIDUgMCBSC"
            + "iAgIC9GaWx0ZXIgL0ZsYXRlRGVjb2RlCj4+CnN0cmVhbQp4nDNUMABCXUMQpWdkopCcy1XIFcgFADCwBFQKZW5"
            + "kc3RyZWFtCmVuZG9iago1IDAgb2JqCiAgIDI3CmVuZG9iagozIDAgb2JqCjw8Cj4+CmVuZG9iagoyIDAgb2JqC"
            + "jw8IC9UeXBlIC9QYWdlICUgMQogICAvUGFyZW50IDEgMCBSCiAgIC9NZWRpYUJveCBbIDAgMCAwLjI0IDAuMjQ"
            + "gXQogICAvQ29udGVudHMgNCAwIFIKICAgL0dyb3VwIDw8CiAgICAgIC9UeXBlIC9Hcm91cAogICAgICAvUyAvV"
            + "HJhbnNwYXJlbmN5CiAgICAgIC9JIHRydWUKICAgICAgL0NTIC9EZXZpY2VSR0IKICAgPj4KICAgL1Jlc291cmN"
            + "lcyAzIDAgUgo+PgplbmRvYmoKMSAwIG9iago8PCAvVHlwZSAvUGFnZXMKICAgL0tpZHMgWyAyIDAgUiBdCiAgI"
            + "C9Db3VudCAxCj4+CmVuZG9iago2IDAgb2JqCjw8IC9Qcm9kdWNlciAoY2Fpcm8gMS4xNS4xMiAoaHR0cDovL2N"
            + "haXJvZ3JhcGhpY3Mub3JnKSkKICAgL0NyZWF0aW9uRGF0ZSAoRDoyMDIyMTIyOTAwNTQzMSswMScwMCkKPj4KZ"
            + "W5kb2JqCjcgMCBvYmoKPDwgL1R5cGUgL0NhdGFsb2cKICAgL1BhZ2VzIDEgMCBSCj4+CmVuZG9iagp4cmVmCjA"
            + "gOAowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDAzODEgMDAwMDAgbiAKMDAwMDAwMDE2MSAwMDAwMCBuIAowM"
            + "DAwMDAwMTQwIDAwMDAwIG4gCjAwMDAwMDAwMTUgMDAwMDAgbiAKMDAwMDAwMDExOSAwMDAwMCBuIAowMDAwMDA"
            + "wNDQ2IDAwMDAwIG4gCjAwMDAwMDA1NjIgMDAwMDAgbiAKdHJhaWxlcgo8PCAvU2l6ZSA4CiAgIC9Sb290IDcgM"
            + "CBSCiAgIC9JbmZvIDYgMCBSCj4+CnN0YXJ0eHJlZgo2MTQKJSVFT0YK";
    private static final String PNG_FILE = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAACXBIW"
            + "XMAAC4jAAAuIwF4pT92AAAAB3RJTUUH5gwcFzcNJCk+ZQAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0l"
            + "NUFeBDhcAAAAMSURBVAjXY/j//z8ABf4C/tzMWecAAAAASUVORK5CYII=";

    public ImportedMoneyflowReceiptTransportBuilder forReceipt1() {
        super.setId(RECEIPT_1ID);
        super.setFilename("1000.jpg");
        super.setMediaType("image/jpeg");
        super.setReceipt(JPEG_FILE);
        return this;
    }

    public ImportedMoneyflowReceiptTransportBuilder forReceipt2() {
        super.setId(RECEIPT_2ID);
        super.setFilename("2000.jpg");
        super.setMediaType("application/pdf");
        super.setReceipt(PDF_FILE);
        return this;
    }

    public ImportedMoneyflowReceiptTransportBuilder forJpegReceipt() {
        super.setId(NEXT_ID);
        super.setFilename("testfile.jpg");
        super.setMediaType("image/jpeg");
        super.setReceipt(JPEG_FILE);
        return this;
    }

    public ImportedMoneyflowReceiptTransportBuilder forPngReceipt() {
        super.setId(NEXT_ID);
        super.setFilename("testfile.png");
        super.setMediaType("image/png");
        super.setReceipt(PNG_FILE);
        return this;
    }

    public ImportedMoneyflowReceiptTransportBuilder forPdfReceipt() {
        super.setId(NEXT_ID);
        super.setFilename("testfile.pdf");
        super.setMediaType("application/pdf");
        super.setReceipt(PDF_FILE);
        return this;
    }

    public ImportedMoneyflowReceiptTransport build() {
        final ImportedMoneyflowReceiptTransport transport = new ImportedMoneyflowReceiptTransport();
        transport.setId(super.getId());
        transport.setFilename(super.getFilename());
        transport.setMediaType(super.getMediaType());
        transport.setReceipt(super.getReceipt());
        return transport;
    }
}
