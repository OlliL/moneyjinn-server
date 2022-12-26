
package org.laladev.moneyjinn.model.etf;

import java.io.Serializable;

public class EtfFlowComparator implements java.util.Comparator<EtfFlow>, Serializable {
  private static final long serialVersionUID = 1L;

  @Override
  public int compare(final EtfFlow o1, final EtfFlow o2) {
    if (o1 == null && o2 == null) {
      return 0;
    } else if (o1 == null) {
      return -1;
    } else if (o2 == null) {
      return 1;
    }
    return o1.getTime().compareTo(o2.getTime());
  }
}
