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

import java.math.BigDecimal;

/**
 * Represents a single HWg-STE measurement.
 *
 * @author mluppi
 */
public class HwgSteMeasurement {

    private final HwgSteMeasurementType type;
    private final HwgSteMeasurementStatus status;
    private final BigDecimal value;
    private final BigDecimal min;
    private final BigDecimal max;

    public HwgSteMeasurement(final HwgSteMeasurementType type, final HwgSteMeasurementStatus status,
            final BigDecimal value, final BigDecimal min, final BigDecimal max) {
        this.type = type;
        this.status = status;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public HwgSteMeasurementStatus getStatus() {
        return status;
    }

    public HwgSteMeasurementType getType() {
        return type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

}
