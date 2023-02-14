//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.TechnicalException;

public class CapitalsourceTypeMapper {
  private static final Integer CURRENT_ASSET_SHORT = 1;
  private static final Integer LONG_TERM_ASSET_SHORT = 2;
  private static final Integer RESERVE_ASSET_SHORT = 3;
  private static final Integer PROVISION_ASSET_SHORT = 4;
  private static final Integer CREDIT_SHORT = 5;

  private CapitalsourceTypeMapper() {
  }

  public static CapitalsourceType map(final Integer type) {
    if (type != null) {
      switch (type) {
        case 1:
          return CapitalsourceType.CURRENT_ASSET;
        case 2:
          return CapitalsourceType.LONG_TERM_ASSET;
        case 3:
          return CapitalsourceType.RESERVE_ASSET;
        case 4:
          return CapitalsourceType.PROVISION_ASSET;
        case 5:
          return CapitalsourceType.CREDIT;
        default:
          throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
      }
    }
    return null;
  }

  public static Integer map(final CapitalsourceType type) {
    if (type != null) {
      switch (type) {
        case CURRENT_ASSET:
          return CURRENT_ASSET_SHORT;
        case LONG_TERM_ASSET:
          return LONG_TERM_ASSET_SHORT;
        case RESERVE_ASSET:
          return RESERVE_ASSET_SHORT;
        case PROVISION_ASSET:
          return PROVISION_ASSET_SHORT;
        case CREDIT:
          return CREDIT_SHORT;
        default:
          throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
      }
    }
    return null;
  }
}
