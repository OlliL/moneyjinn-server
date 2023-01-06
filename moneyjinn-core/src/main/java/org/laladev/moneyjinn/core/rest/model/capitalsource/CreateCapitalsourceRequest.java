
package org.laladev.moneyjinn.core.rest.model.capitalsource;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@XmlRootElement(name = "createCapitalsourceRequest")
public class CreateCapitalsourceRequest extends AbstractCapitalsourceRequest {
}
