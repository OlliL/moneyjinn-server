package org.laladev.moneyjinn.core.rest.model.user;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("createUserResponse")
public class CreateUserResponse extends AbstractCreateUserResponse {
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowCreateUserResponse [getGroupTransports()=");
		builder.append(this.getGroupTransports());
		builder.append(", getResult()=");
		builder.append(this.getResult());
		builder.append(", getValidationItemTransports()=");
		builder.append(this.getValidationItemTransports());
		builder.append("]");
		return builder.toString();
	}

}
