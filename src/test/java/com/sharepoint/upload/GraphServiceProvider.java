package com.sharepoint.upload;

import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.auth.publicClient.UsernamePasswordProvider;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

import java.io.IOException;

public class GraphServiceProvider {

    private static GraphServiceProvider provider = null;

    public GraphServiceProvider(){ }

    public static GraphServiceProvider getInstance(){
        if (provider == null) {
            provider = new GraphServiceProvider();
        }
        return provider;
    }

    public IGraphServiceClient getGraphServiceProvider() throws IOException {

        UsernamePasswordProvider authProvider = new UsernamePasswordProvider(
                SharepointCredentials.getClientId(),
                SharepointCredentials.getScopeList(),
                SharepointCredentials.getUsername(),
                SharepointCredentials.getPassword(),
                NationalCloud.Global,
                SharepointCredentials.getTenantId(),
                SharepointCredentials.getClientSecret());

        IGraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(authProvider)
                        .buildClient();

        return graphClient;
    }
}
