package org.donghyuns.oauth.validator.filter;

import org.springframework.stereotype.Component;

@Component
public class MaintenanceModeManager {
  private boolean maintenanceMode = false;

  public boolean isMaintenanceMode() {
    return maintenanceMode;
  }

  public void setMaintenanceMode(boolean maintenanceMode) {
    this.maintenanceMode = maintenanceMode;
  }
}
