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

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.tractusx.semantics.accesscontrol.sql.model.AccessRulePolicy;
import org.eclipse.tractusx.semantics.accesscontrol.sql.rest.model.AasPolicy;
import org.eclipse.tractusx.semantics.accesscontrol.sql.rest.model.AccessRuleValue;
import org.eclipse.tractusx.semantics.accesscontrol.sql.rest.model.AccessRuleValues;
import org.eclipse.tractusx.semantics.accesscontrol.sql.rest.model.CreateAccessRule;
import org.eclipse.tractusx.semantics.accesscontrol.sql.rest.model.OperatorType;
import org.eclipse.tractusx.semantics.accesscontrol.sql.rest.model.PolicyType;

public class RuleGenerator {

   public CreateAccessRule generateRule( String targetTenant,
         Map<String, String> mandatorySpecificAssetIds,
         Set<String> visibleSpecificAssetIdNames,
         Set<String> visibleSemanticIds,
         OffsetDateTime validFrom,
         OffsetDateTime validTo ) {
      return new CreateAccessRule()
            .description( "Description" )
            .policyType( PolicyType.AAS )
            .policy( new AasPolicy()
                  .addAccessRulesItem( new AccessRuleValues()
                        .attribute( AccessRulePolicy.BPN_RULE_NAME )
                        .operator( OperatorType.EQ )
                        .value( targetTenant ) )
                  .addAccessRulesItem( new AccessRuleValues()
                        .attribute( AccessRulePolicy.MANDATORY_SPECIFIC_ASSET_IDS_RULE_NAME )
                        .operator( OperatorType.INCLUDES )
                        .values( mandatorySpecificAssetIds.entrySet().stream()
                              .map( entry -> new AccessRuleValue()
                                    .attribute( entry.getKey() )
                                    .operator( OperatorType.EQ )
                                    .value( entry.getValue() ) )
                              .collect( Collectors.toSet() ) ) )
                  .addAccessRulesItem( new AccessRuleValues()
                        .attribute( AccessRulePolicy.VISIBLE_SPECIFIC_ASSET_ID_NAMES_RULE_NAME )
                        .operator( OperatorType.INCLUDES )
                        .values( visibleSpecificAssetIdNames.stream()
                              .map( item -> new AccessRuleValue()
                                    .attribute( "name" )
                                    .operator( OperatorType.EQ )
                                    .value( item ) )
                              .collect( Collectors.toSet() ) ) )
                  .addAccessRulesItem( new AccessRuleValues()
                        .attribute( AccessRulePolicy.VISIBLE_SEMANTIC_IDS_RULE_NAME )
                        .operator( OperatorType.INCLUDES )
                        .values( visibleSemanticIds.stream()
                              .map( item -> new AccessRuleValue()
                                    .attribute( "modelUrn" )
                                    .operator( OperatorType.EQ )
                                    .value( item ) )
                              .collect( Collectors.toSet() ) ) )
            )
            .validFrom( validFrom )
            .validTo( validTo );
   }
}
