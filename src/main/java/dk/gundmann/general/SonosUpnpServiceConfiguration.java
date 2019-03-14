package dk.gundmann.general;

import org.fourthline.cling.DefaultUpnpServiceConfiguration;
import org.fourthline.cling.transport.impl.StreamClientConfigurationImpl;
import org.fourthline.cling.transport.impl.StreamClientImpl;
import org.fourthline.cling.transport.spi.StreamClient;

public class SonosUpnpServiceConfiguration extends DefaultUpnpServiceConfiguration {

    @Override
    public StreamClient<?> createStreamClient() {
        return new StreamClientImpl(new StreamClientConfigurationImpl(getSyncProtocolExecutorService()));
    }
    
}
