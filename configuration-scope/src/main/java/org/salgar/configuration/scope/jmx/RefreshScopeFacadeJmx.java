package org.salgar.configuration.scope.jmx;

import org.salgar.configuration.api.RefreshScopeFacade;
import org.salgar.configuration.scope.RefreshScope;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "configuration:name=configuration-service,type=org.salgar.scope.RefreshScope,artifactId=configuration-service")
public class RefreshScopeFacadeJmx implements RefreshScopeFacade {
    private RefreshScope refreshScope;

    public RefreshScopeFacadeJmx(RefreshScope refreshScope) {
        this.refreshScope = refreshScope;
    }

    @Override
    @ManagedOperation(description = "delivers the configuration parameters")
    public void refresh() {
        refreshScope.destroy();
    }
}
