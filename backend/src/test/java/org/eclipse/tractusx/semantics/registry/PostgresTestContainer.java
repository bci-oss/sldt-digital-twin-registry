/********************************************************************************
 * Copyright (c) 2021-2022 Robert Bosch Manufacturing Solutions GmbH
 * Copyright (c) 2021-2022 Contributors to the Eclipse Foundation
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
 ********************************************************************************/
package org.eclipse.tractusx.semantics.registry;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer {

   private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");


   static {
      postgresContainer.start();
   }

   @DynamicPropertySource
   static void databaseProperties( DynamicPropertyRegistry registry ) {
      registry.add("spring.datasource.driverClassName", postgresContainer::getDriverClassName);
      registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
      registry.add("spring.datasource.username", postgresContainer::getUsername);
      registry.add("spring.datasource.password", postgresContainer::getPassword);
   }
}
