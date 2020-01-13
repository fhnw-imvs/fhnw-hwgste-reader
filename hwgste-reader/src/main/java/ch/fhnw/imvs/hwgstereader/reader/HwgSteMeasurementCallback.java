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
package ch.fhnw.imvs.hwgstereader.reader;

import ch.fhnw.imvs.hwgstereader.api.HwgSteReading;

import java.util.List;

/**
 * Interface to be implemented by the HWg-STE callback that handles the readings.
 *
 * @author mluppi
 */
@FunctionalInterface
public interface HwgSteMeasurementCallback {

    /**
     * @param readings the fetched HWg-STE readings
     */
    void handle(final List<HwgSteReading> readings);

}
