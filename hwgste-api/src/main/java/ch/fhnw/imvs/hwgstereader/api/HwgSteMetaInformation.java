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

/**
 * Represents meta information about a HWg-STE node.
 *
 * @author mluppi
 */
public class HwgSteMetaInformation {

    private final String deviceName;
    private final String ipAddress;
    private final String macAddress;
    private final String systemName;
    private final String systemLocation;

    public HwgSteMetaInformation(final String deviceName, final String ipAddress, final String macAddress,
                                 final String systemName, final String systemLocation) {
        this.deviceName = deviceName;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.systemName = systemName;
        this.systemLocation = systemLocation;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getSystemLocation() {
        return systemLocation;
    }
}
