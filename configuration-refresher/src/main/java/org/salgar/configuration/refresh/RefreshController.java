package org.salgar.configuration.refresh;

import org.salgar.configuration.api.ConfigurationFacade;
import org.salgar.configuration.api.RefreshScopeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Named;

@RestController
public class RefreshController {
    @Autowired(required = false)
    @Named("proxyRefreshScopeFacade")
    private RefreshScopeFacade refreshScopeFacade;

    @Autowired(required = false)
    @Named("proxyConfigurationFacade")
    private ConfigurationFacade configurationFacade;


    @RequestMapping("/refresh")
    public void refresh() {
        if(refreshScopeFacade != null) {
            refreshScopeFacade.refresh();
        }
        if(configurationFacade != null) {
            configurationFacade.refresh();
        }
    }
}
