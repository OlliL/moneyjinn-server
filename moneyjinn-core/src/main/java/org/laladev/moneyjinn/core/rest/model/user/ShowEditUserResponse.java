package org.laladev.moneyjinn.core.rest.model.user;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "showEditUserResponse")
public class ShowEditUserResponse extends AbstractShowUserResponse {

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowEditUserResponse [getUserTransport()=");
		builder.append(this.getUserTransport());
		builder.append(", getAccessRelationTransports()=");
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
