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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.InputStream;

/**
 * Parser for HWg-STE XML data
 *
 * @author mluppi
 */
final class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * Parses an {@link InputStream} of XML data.
     *
     * @param xml An {@link InputStream} referencing XML data.
     * @param clazz The target class to be parsed.
     * @param <T> The type of the target class to be parsed.
     * @return The parsed class ot null if an error occurred.
     */
    static <T> T parseXml(final InputStream xml, Class clazz) {
        try {
            final JAXBContext context = JAXBContext.newInstance(clazz);

            @SuppressWarnings("unchecked")
            final JAXBElement<T> element = ((JAXBElement<T>) context.createUnmarshaller().unmarshal(xml));

            return element.getValue();
        } catch (Exception e) {
            logger.error("Could not parse XML", e);
            return null;
        }
    }

    private Parser() {
    }
}
