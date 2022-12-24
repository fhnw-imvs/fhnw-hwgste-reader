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

import ch.fhnw.imvs.hwgstereader.api.schemas.SensorRootType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mapper for JAXB root object of HWg-STE node that maps to {@link HwgSteMeasurement} objects
 *
 * @author mluppi
 */
final class Mapper {

    private static final Logger logger = LoggerFactory.getLogger(Mapper.class);

    private static final int TYPE_ID_TEMPERATURE = 215;
    private static final int TYPE_ID_HUMIDITY = 216;

    private static final int VALID_STATE = 1;

    /**
     * Maps a {@link SensorRootType} to {@link HwgSteMetaInformation}.
     *
     * @param root The {@link SensorRootType} object to map.
     * @return A {@link HwgSteMetaInformation} with the meta information or {@code null}
     * if no data present or nothing could be read due to missing elements in {@code root}.
     */
    static HwgSteMetaInformation mapMetaInformation(final SensorRootType root) {
        if ((root == null) || (root.getAgent() == null)) {
            return null;
        }
        final SensorRootType.Agent agent = root.getAgent();
        return new HwgSteMetaInformation(agent.getDeviceName(), agent.getIP(), agent.getMAC(),
                agent.getSysName(), agent.getSysLocation());
    }

    /**
     * Maps a {@link SensorRootType} to a {@link List<HwgSteMeasurement>}.
     *
     * @param root The {@link SensorRootType} object to map.
     * @return An {@link Optional} containing a {@link List<HwgSteMeasurement>}
     * or is empty when no data could be mapped due to missing elements in {@code root}.
     */
    static Optional<List<HwgSteMeasurement>> mapMeasurements(final SensorRootType root) {
        if ((root == null) || (root.getSenSet() == null) || (root.getSenSet().getEntry() == null)) {
            return Optional.empty();
        }
        final List<HwgSteMeasurement> measurements = new ArrayList<>();
        for (final SensorRootType.SenSet.Entry entry : root.getSenSet().getEntry()) {
            final HwgSteMeasurement measurement = getMeasurement(entry);
            if (measurement != null) {
                measurements.add(measurement);
            }
        }
        return Optional.of(measurements);
    }

    private static HwgSteMeasurement getMeasurement(final SensorRootType.SenSet.Entry entry) {
        if (entry != null) {
            final HwgSteMeasurementType type = getType(entry.getID());
            if (type != null) {
                return new HwgSteMeasurement(type,
                        (entry.getState() == VALID_STATE) ? HwgSteMeasurementStatus.VALID : HwgSteMeasurementStatus.INVALID,
                        entry.getValue(), entry.getMin(), entry.getMax());
            } else {
                return new HwgSteMeasurement(HwgSteMeasurementType.UNKNOWN, HwgSteMeasurementStatus.ERROR, null, null, null);
            }
        }
        return null;
    }

    private static HwgSteMeasurementType getType(final int sensorTypeId) {
        switch (sensorTypeId) {
            case TYPE_ID_TEMPERATURE:
                return HwgSteMeasurementType.TEMPERATURE;
            case TYPE_ID_HUMIDITY:
                return HwgSteMeasurementType.HUMIDITY;
            default:
                logger.warn("No mapping for sensorTypeId={}", sensorTypeId);
                return null;
        }
    }

    private Mapper() {
    }
}
