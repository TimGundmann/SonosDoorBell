package dk.gundmann.general;

import org.fourthline.cling.DefaultUpnpServiceConfiguration;
import org.fourthline.cling.transport.impl.apache.StreamClientConfigurationImpl;
import org.fourthline.cling.transport.impl.apache.StreamClientImpl;
import org.fourthline.cling.transport.spi.StreamClient;

public class BellUpnpServiceConfiguration extends DefaultUpnpServiceConfiguration {

    @Override
    public StreamClient<?> createStreamClient() {
        return new StreamClientImpl(new StreamClientConfigurationImpl(getSyncProtocolExecutorService()));
    }
    
}
