/*
Copyright 2019 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)

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

import java.util.Collections;
import java.util.List;

/**
 * Represents the reading of a single node including status and measurements.
 *
 * @author mluppi
 */
public class HwgSteReading {

    private final HwgSteNode node;
    private final HwgSteFetchStatus status;
    private final HwgSteMetaInformation metaInformation;
    private final List<HwgSteMeasurement> measurements;

    public HwgSteReading(final HwgSteNode node, final HwgSteFetchStatus status) {
        this.node = node;
        this.status = status;
        this.metaInformation = null;
        this.measurements = Collections.emptyList();
    }

    public HwgSteReading(final HwgSteNode node, final HwgSteFetchStatus status,
                         final HwgSteMetaInformation metaInformation, final List<HwgSteMeasurement> measurements) {
        this.node = node;
        this.status = status;
        this.metaInformation = metaInformation;
        this.measurements = measurements;
    }

    public HwgSteNode getNode() {
        return node;
    }

    public HwgSteFetchStatus getStatus() {
        return status;
    }

    public HwgSteMetaInformation getMetaInformation() {
        return metaInformation;
    }

    public List<HwgSteMeasurement> getMeasurements() {
        return measurements;
    }
}
