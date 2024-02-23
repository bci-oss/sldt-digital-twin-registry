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

package org.eclipse.tractusx.semantics.registry.performance;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import org.eclipse.tractusx.semantics.aas.registry.model.AssetAdministrationShellDescriptor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsertRecordsMain {

   private static final String MANUFACTURER_PART_ID = "manufacturerPartId";
   private static final String CUSTOMER_PART_ID = "customerPartId";
   private static final String PART_INSTANCE_ID = "partInstanceId";
   private static final String SEMANTIC_ID_1 = "semanticId1";
   private static final String SEMANTIC_ID_2 = "semanticId2";
   private static final String SEMANTIC_ID_3 = "semanticId3";
   private static final String SUBMODEL_URL_1 = "http://example.com/1";
   private static final String SUBMODEL_URL_2 = "http://example.com/2";
   private static final String SUBMODEL_URL_3 = "http://example.com/3";
   private static final String BASE_URL = "http://localhost:4243/api/v3.0";
   private static final String SHELL_CREATE_PATH = "/shell-descriptors";
   private static final String RULE_CREATE_PATH = "/access-controls/rules";
   private static final String BPN_A = "BPNL00000000000A";
   private static final String BPN_B = "BPNL00000000000B";
   private static final String BPN_C = "BPNL00000000000C";
   private static final String BPN_D = "BPNL00000000000D";
   private static final String BPN_E = "BPNL00000000000E";
   private static final String BPN_F = "BPNL00000000000F";
   private static final String BPN_G = "BPNL00000000000G";
   private static final String BPN_H = "BPNL00000000000H";
   private static final String BPN_I = "BPNL00000000000I";
   private static final String BPN_J = "BPNL00000000000J";
   private static final String BPN_K = "BPNL00000000000K";
   private static final String BPN_L = "BPNL00000000000L";
   private static final String BPN_M = "BPNL00000000000M";
   private static final String BPN_N = "BPNL00000000000N";
   private static final String BPN_O = "BPNL00000000000O";
   private static final String BPN_P = "BPNL00000000000P";
   private static final String BPN_Q = "BPNL00000000000Q";
   private static final String BPN_R = "BPNL00000000000R";
   private static final String BPN_S = "BPNL00000000000S";
   private static final String BPN_T = "BPNL00000000000T";
   private static final RuleGenerator RULE_GENERATOR = new RuleGenerator();
   private static final ShellGenerator SHELL_GENERATOR = new ShellGenerator();

   public static void main( String[] args ) {
      final var shellCounter = new AtomicLong( 0L );
      final var ruleCounter = new AtomicLong( 0L );
      final var allBpns = List.of(
            BPN_A, BPN_B, BPN_C, BPN_D, BPN_E,
            BPN_F, BPN_G, BPN_H, BPN_I, BPN_J,
            BPN_K, BPN_L, BPN_M, BPN_N, BPN_O,
            BPN_P, BPN_Q, BPN_R, BPN_S, BPN_T );
      Map<String, String> valueMap = Map.of( "00001", "991", "11111", "881", "22221", "771", "33331", "661",
            "44441", "551", "55551", "441", "66661", "331", "77771", "221",
            "88881", "111", "99991", "001" );
      RestClient restClient = RestClient.create();
      insertAllRules( valueMap, allBpns, restClient, ruleCounter );
      bulkInsertShells( valueMap, 500, allBpns, restClient, shellCounter );
      bulkInsertShells( valueMap, 100, allBpns, restClient, shellCounter );
      bulkInsertShells( valueMap, 200, allBpns, restClient, shellCounter );
      bulkInsertShells( valueMap, 300, allBpns, restClient, shellCounter );
      bulkInsertShells( valueMap, 400, allBpns, restClient, shellCounter );
      System.exit( 0 );
   }

   private static void insertAllRules(
         Map<String, String> valueMap, List<String> allBpns, RestClient restClient, AtomicLong counter ) {
      valueMap.forEach( ( k, v ) -> allBpns.parallelStream()
            .forEach( bpn -> insertRuleForUser( bpn, k, bpn + v, restClient, counter ) ) );
   }

   private static void bulkInsertShells( Map<String, String> valueMap, int batchCount, List<String> allBpns, RestClient restClient, AtomicLong counter ) {
      valueMap.forEach( ( k, v ) -> allBpns.parallelStream()
            .forEach( bpn -> insertShellsForUser( batchCount, k, bpn + v, bpn, restClient, counter ) ) );
   }

   private static void insertShellsForUser(
         int numberOfShells, String manufPartId, String custPartId, String bpn, RestClient restClient, AtomicLong counter ) {
      List<UUID> uuids = IntStream.rangeClosed( 1, numberOfShells )
            .mapToObj( i -> UUID.randomUUID() )
            .toList();
      final var shells = SHELL_GENERATOR.generateShells( uuids,
            Map.of( MANUFACTURER_PART_ID, manufPartId, CUSTOMER_PART_ID, custPartId ),
            PART_INSTANCE_ID,
            Map.of( MANUFACTURER_PART_ID, List.of( bpn ),
                  CUSTOMER_PART_ID, List.of( bpn ),
                  PART_INSTANCE_ID, List.of() ),
            Map.of( SEMANTIC_ID_1, SUBMODEL_URL_1, SEMANTIC_ID_2, SUBMODEL_URL_2, SEMANTIC_ID_3, SUBMODEL_URL_3 )
      );
      for ( final AssetAdministrationShellDescriptor shell : shells ) {
         ResponseEntity<Void> entity = restClient
               .post()
               .uri( URI.create( BASE_URL + SHELL_CREATE_PATH ) )
               .body( shell )
               .contentType( MediaType.APPLICATION_JSON )
               .retrieve().toBodilessEntity();
         HttpStatusCode statusCode = entity.getStatusCode();
         if ( !statusCode.is2xxSuccessful() ) {
            log.error( "Error! Couldn't insert shell!" );
         }
         long count = counter.incrementAndGet();
         if ( count % 1000 == 0 ) {
            log.info( "Inserted {} shells", count );
         }
      }
   }

   private static void insertRuleForUser(
         String bpn, String manufPartId, String custPartId, RestClient restClient, AtomicLong counter ) {
      final var rule = RULE_GENERATOR.generateRule( bpn,
            Map.of( MANUFACTURER_PART_ID, manufPartId, CUSTOMER_PART_ID, custPartId ),
            Set.of( MANUFACTURER_PART_ID, CUSTOMER_PART_ID ),
            Set.of( SEMANTIC_ID_1, SEMANTIC_ID_2 ),
            null,
            null
      );
      ResponseEntity<Void> entity = restClient
            .post()
            .uri( URI.create( BASE_URL + RULE_CREATE_PATH ) )
            .body( rule )
            .contentType( MediaType.APPLICATION_JSON )
            .retrieve().toBodilessEntity();
      HttpStatusCode statusCode = entity.getStatusCode();
      if ( !statusCode.is2xxSuccessful() ) {
         log.error( "Error! Couldn't insert rule!" );
      }
      long count = counter.incrementAndGet();
      if ( count % 10 == 0 ) {
         log.info( "Inserted {} rules", count );
      }
   }
}
