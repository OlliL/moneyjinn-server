package org.laladev.moneyjinn.core.rest.model.user;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "updateUserResponse")
public class UpdateUserResponse extends AbstractUpdateUserResponse {

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("UpdateUserResponse [getAccessRelationTransports()=");
		builder.append(this.getAccessRelationTransports());
		builder.append(", getGroupTransports()=");
		builder.append(this.getGroupTransports());
		builder.append(", getResult()=");
		builder.append(this.getResult());
		builder.append(", getValidationItemTransports()=");
		builder.append(this.getValidationItemTransports());
		builder.append("]");
		return builder.toString();
	}

}
