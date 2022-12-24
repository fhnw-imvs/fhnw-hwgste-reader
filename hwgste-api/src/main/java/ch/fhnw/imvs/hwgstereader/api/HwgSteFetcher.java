/*
Copyright 2019-2022 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package ch.fhnw.imvs.hwgstereader.api;

import ch.fhnw.imvs.hwgstereader.api.schemas.ObjectFactory;
import ch.fhnw.imvs.hwgstereader.api.schemas.SensorRootType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Fetcher for HWg-STE sensor nodes.
 *
 * @author mluppi
 */
public class HwgSteFetcher {

    private static final Logger logger = LoggerFactory.getLogger(HwgSteFetcher.class);

    private static final String PROTOCOL = "http://";
    private static final String XML_PATH = "/values.xml"; // NOSONAR

    private int connectTimeout = 250; // in milliseconds
    private int readTimeout = 250; // in milliseconds

    /**
     * Fetches data from given HWg-STE node.
     *
     * @param nodes the {@link Collection<HwgSteNode>} to fetch data from
     */
    public List<HwgSteReading> fetch(final Collection<HwgSteNode> nodes) {
        final List<HwgSteReading> map = new ArrayList<>();
        logger.debug("Starting fetch cycle for {} nodes", nodes.size());
        final long startTime = System.currentTimeMillis();
        int i = 1;
        for (final HwgSteNode node : nodes) {
            logger.debug("Processing node {} of {}", i++, nodes.size());
            map.add(fetch(node));
        }
        logger.debug("Ended fetch cycle in {}ms", System.currentTimeMillis() - startTime);
        return map;
    }

    /**
     * Fetches data from given HWg-STE node.
     *
     * @param node the node to fetch data from
     */
    public HwgSteReading fetch(final HwgSteNode node) {
        try {
            long startTime = System.currentTimeMillis();

            // set up connection to get XML from node
            final URLConnection connection = connectToUrl(node);

            // parse XML and map to API
            final SensorRootType root = Parser.parseXml(connection.getInputStream(), ObjectFactory.class);
            final HwgSteMetaInformation metaInformation = Mapper.mapMetaInformation(root);
            final Optional<List<HwgSteMeasurement>> measurements = Mapper.mapMeasurements(root);

            logger.debug("Fetching values for node={} took {}ms", node.getName(), System.currentTimeMillis() - startTime);

            if (measurements.isPresent()) {
                return new HwgSteReading(node, HwgSteFetchStatus.SUCCESS, metaInformation, measurements.get());
            }

        } catch (Exception e) {
            logger.warn("Fetching values for node={} failed", node.getName(), e);
        }

        return new HwgSteReading(node, HwgSteFetchStatus.ERROR);
    }

    /**
     * Returns the setting for connect timeout.
     *
     * @return an {@code int} that indicates the connect timeout value in milliseconds
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Specifies the read timeout in milliseconds to be used when opening a communications link to a sensor.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * @param connectTimeout an {@code int} that specifies the timeout value to be used in milliseconds
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Returns the setting for read timeout.
     *
     * @return an {@code int} that indicates the read timeout value in milliseconds
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Specifies the read timeout in milliseconds to be used when reading from a sensor.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * @param readTimeout an {@code int} that specifies the timeout value to be used in milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * Returns a open and configured communication link for a given node.
     *
     * @param node a {@link HwgSteNode} to connect to
     * @return a {@link URLConnection} representing an open communication link with the node
     * @throws IOException if an I/O exception occurs
     */
    URLConnection connectToUrl(HwgSteNode node) throws IOException {
        final URL url = new URL(PROTOCOL + node.getHostname() + XML_PATH);
        final URLConnection connection = url.openConnection();
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
        return connection;
    }
}
