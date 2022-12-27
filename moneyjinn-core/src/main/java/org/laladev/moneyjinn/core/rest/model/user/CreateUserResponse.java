
package org.laladev.moneyjinn.core.rest.model.user;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "createUserResponse")
public class CreateUserResponse extends AbstractCreateUserResponse {
  @Override
  public String toString() {
    return "CreateUserResponse [getUserId()=" + this.getUserId() + ", getGroupTransports()="
        + this.getGroupTransports() + ", getResult()=" + this.getResult()
        + ", getValidationItemTransports()=" + this.getValidationItemTransports() + "]";
  }
}
