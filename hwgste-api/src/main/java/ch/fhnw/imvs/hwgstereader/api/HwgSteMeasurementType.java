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

/**
 * The type of a single HWg-STE measurement.
 *
 * @author mluppi
 */
public enum HwgSteMeasurementType {

    TEMPERATURE("°C"),
    HUMIDITY("%RH"),
    UNKNOWN("");

    private final String unit;

    HwgSteMeasurementType(final String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

}
