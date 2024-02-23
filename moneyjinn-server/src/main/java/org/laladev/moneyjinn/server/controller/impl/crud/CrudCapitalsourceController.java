package org.laladev.moneyjinn.server.controller.impl.crud;

import java.util.List;

import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudCapitalsourceControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CrudCapitalsourceController extends AbstractController implements CrudCapitalsourceControllerApi {
	private final IAccessRelationService accessRelationService;
	private final ICapitalsourceService capitalsourceService;
	private final IUserService userService;
	private final CapitalsourceTransportMapper capitalsourceTransportMapper;
	private final ValidationItemTransportMapper validationItemTransportMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		this.registerBeanMapper(this.capitalsourceTransportMapper);
		this.registerBeanMapper(this.validationItemTransportMapper);
	}

	@Override
	public ResponseEntity<List<CapitalsourceTransport>> readAll() {
		final UserID userId = super.getUserId();
		final List<Capitalsource> capitalsources = this.capitalsourceService.getAllCapitalsources(userId);

		return ResponseEntity.ok(super.mapList(capitalsources, CapitalsourceTransport.class));
	}

	@Override
	public ResponseEntity<CapitalsourceTransport> readOne(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final Group group = this.accessRelationService.getCurrentGroup(userId);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(id);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, group.getId(),
				capitalsourceId);

		if (capitalsource == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(super.map(capitalsource, CapitalsourceTransport.class));
	}

	@Override
	public ResponseEntity<CapitalsourceTransport> create(
			@RequestBody final CapitalsourceTransport capitalsourceTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final Capitalsource capitalsource = super.map(capitalsourceTransport, Capitalsource.class);
		final User user = this.userService.getUserById(userId);
		final Group accessor = this.accessRelationService.getCurrentGroup(userId);
		capitalsource.setId(null);
		capitalsource.setUser(user);
		capitalsource.setAccess(accessor);
		final ValidationResult validationResult = this.capitalsourceService.validateCapitalsource(capitalsource);

		this.throwValidationExceptionIfInvalid(validationResult);

		final CapitalsourceID capitalsourceId = this.capitalsourceService.createCapitalsource(capitalsource);

		capitalsource.setId(capitalsourceId);

		return this.preferedReturn(prefer, capitalsource, CapitalsourceTransport.class);

	}

	@Override
	public ResponseEntity<CapitalsourceTransport> update(
			@RequestBody final CapitalsourceTransport capitalsourceTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final Capitalsource capitalsource = super.map(capitalsourceTransport, Capitalsource.class);
		final User user = this.userService.getUserById(userId);
		final Group accessor = this.accessRelationService.getCurrentGroup(userId);
		capitalsource.setUser(user);
		capitalsource.setAccess(accessor);
		final ValidationResult validationResult = this.capitalsourceService.validateCapitalsource(capitalsource);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.capitalsourceService.updateCapitalsource(capitalsource);

		return this.preferedReturn(prefer, capitalsource, CapitalsourceTransport.class);
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final Group accessor = this.accessRelationService.getCurrentGroup(userId);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(id);

		this.capitalsourceService.deleteCapitalsource(userId, accessor.getId(), capitalsourceId);

		return ResponseEntity.noContent().build();
	}
}
