/*******************************************************************************
 * Copyright (c) 2024 Robert Bosch Manufacturing Solutions GmbH and others
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package org.eclipse.tractusx.semantics.edc.dataplane.http.accesscontrol;

import static org.eclipse.tractusx.semantics.edc.dataplane.http.accesscontrol.DtrDataPlaneAccessControlConfigExtension.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.edc.spi.system.ServiceExtensionContext;

public class HttpAccessControlCheckClientConfig {

   private final Map<String, HttpAccessControlCheckDtrClientConfig> dtrClientConfigMap;
   private final String edcDataPlaneBaseUrl;
   private String edcDataPlanePublicEndpointPath;

   public HttpAccessControlCheckClientConfig( final ServiceExtensionContext context ) {
      dtrClientConfigMap = Arrays.stream( context.getSetting( EDC_DTR_CONFIG_NAMES, "" ).split( "," ) )
            .filter( StringUtils::isNotBlank )
            .collect( Collectors.toUnmodifiableMap( Function.identity(),
                  name -> new HttpAccessControlCheckDtrClientConfig(
                        context.getConfig( EDC_DTR_CONFIG_PREFIX + name ) ) ) );
      edcDataPlaneBaseUrl = context.getSetting( EDC_DATA_PLANE_BASE_URL, null );
      edcDataPlanePublicEndpointPath = context.getSetting( "web.http.public.path", "/api/public" );
   }

   public Map<String, HttpAccessControlCheckDtrClientConfig> getDtrClientConfigMap() {
      return dtrClientConfigMap;
   }

   public String getEdcDataPlaneBaseUrl() {
      return edcDataPlaneBaseUrl;
   }

   public String getEdcDataPlanePublicEndpointPath() {
      return edcDataPlanePublicEndpointPath;
   }

   public void setEdcDataPlanePublicEndpointPath( String edcDataPlanePublicEndpointPath ) {
      this.edcDataPlanePublicEndpointPath = edcDataPlanePublicEndpointPath;
   }
}
