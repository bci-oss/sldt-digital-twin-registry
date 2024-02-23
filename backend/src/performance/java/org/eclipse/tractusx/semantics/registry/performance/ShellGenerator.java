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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.tractusx.semantics.aas.registry.model.AssetAdministrationShellDescriptor;
import org.eclipse.tractusx.semantics.aas.registry.model.AssetKind;
import org.eclipse.tractusx.semantics.aas.registry.model.Endpoint;
import org.eclipse.tractusx.semantics.aas.registry.model.Key;
import org.eclipse.tractusx.semantics.aas.registry.model.KeyTypes;
import org.eclipse.tractusx.semantics.aas.registry.model.LangStringNameType;
import org.eclipse.tractusx.semantics.aas.registry.model.LangStringTextType;
import org.eclipse.tractusx.semantics.aas.registry.model.ProtocolInformation;
import org.eclipse.tractusx.semantics.aas.registry.model.ProtocolInformationSecurityAttributes;
import org.eclipse.tractusx.semantics.aas.registry.model.Reference;
import org.eclipse.tractusx.semantics.aas.registry.model.ReferenceTypes;
import org.eclipse.tractusx.semantics.aas.registry.model.SpecificAssetId;
import org.eclipse.tractusx.semantics.aas.registry.model.SubmodelDescriptor;

public class ShellGenerator {

   public List<AssetAdministrationShellDescriptor> generateShells(
         List<UUID> externalIds,
         Map<String, String> specificAssetIdMap,
         String counterSpecificAssetIdName,
         Map<String, List<String>> specificAssetIdNameExternalSubjetIdsMap,
         Map<String, String> semanticIdToSubmodelUrlMap ) {
      final List<AssetAdministrationShellDescriptor> shells = new ArrayList<>( externalIds.size() );
      for ( int i = 0; i < externalIds.size(); i++ ) {
         final UUID id = externalIds.get( i );
         final Map<String, String> said = new HashMap<>( specificAssetIdMap );
         said.put( counterSpecificAssetIdName, "value_" + i );
         shells.add( generateShell( id, said, specificAssetIdNameExternalSubjetIdsMap, semanticIdToSubmodelUrlMap ) );
      }
      return shells;
   }

   public AssetAdministrationShellDescriptor generateShell(
         UUID externalId,
         Map<String, String> specificAssetIdMap,
         Map<String, List<String>> specificAssetIdNameExternalSubjetIdsMap,
         Map<String, String> semanticIdToSubmodelUrlMap ) {
      AssetAdministrationShellDescriptor assetAdministrationShellDescriptor = new AssetAdministrationShellDescriptor();
      assetAdministrationShellDescriptor.setDisplayName( generateDisplayNames() );
      assetAdministrationShellDescriptor.setGlobalAssetId( "globalAssetId example" );
      assetAdministrationShellDescriptor.setAssetType( "AssetType" );
      assetAdministrationShellDescriptor.setAssetKind( AssetKind.INSTANCE );
      assetAdministrationShellDescriptor.setId( externalId.toString() );
      assetAdministrationShellDescriptor.setIdShort( RandomStringUtils.random( 10, true, true ) );
      assetAdministrationShellDescriptor.setSpecificAssetIds( specificAssetIdMap.entrySet().stream()
            .map( entry -> createSpecificAssetId( entry.getKey(), entry.getValue(),
                  specificAssetIdNameExternalSubjetIdsMap.getOrDefault( entry.getKey(), List.of() ) ) )
            .toList() );
      assetAdministrationShellDescriptor.setDescription( generateDescriptions() );
      assetAdministrationShellDescriptor.setSubmodelDescriptors( semanticIdToSubmodelUrlMap.entrySet().stream()
            .map( entry -> createSubmodelDescriptor( entry.getKey(), entry.getValue() ) )
            .toList() );
      return assetAdministrationShellDescriptor;
   }

   private List<LangStringTextType> generateDescriptions() {
      return Stream.of( "en", "de" )
            .map( l -> new LangStringTextType().language( l ).text( "description - " + l ) )
            .toList();
   }

   private List<LangStringNameType> generateDisplayNames() {
      return Stream.of( "en", "de" )
            .map( l -> new LangStringNameType().language( l ).text( "this is a display name - " + l ) )
            .toList();
   }

   private SpecificAssetId createSpecificAssetId( String name, String value, List<String> externalSubjectIds ) {
      SpecificAssetId specificAssetId = new SpecificAssetId();
      specificAssetId.setName( name );
      specificAssetId.setValue( value );

      if ( externalSubjectIds != null && !externalSubjectIds.isEmpty() ) {
         Reference reference = new Reference();
         reference.setType( ReferenceTypes.EXTERNALREFERENCE );
         List<Key> keys = new ArrayList<>();
         externalSubjectIds.forEach( externalSubjectId -> {
            Key key = new Key();
            key.setType( KeyTypes.SUBMODEL );
            key.setValue( externalSubjectId );
            keys.add( key );
         } );
         reference.setKeys( keys );
         specificAssetId.setExternalSubjectId( reference );
      }
      return specificAssetId;
   }

   private SubmodelDescriptor createSubmodelDescriptor( String semanticId, String endpointUrl ) {
      ProtocolInformation protocolInformation = new ProtocolInformation();
      protocolInformation.setEndpointProtocol( "endpointProtocolExample" );
      protocolInformation.setHref( endpointUrl );
      protocolInformation.setEndpointProtocolVersion( List.of( "e" ) );
      protocolInformation.setSubprotocol( "subprotocolExample" );
      protocolInformation.setSubprotocolBody( "subprotocolBodyExample" );
      protocolInformation.setSubprotocolBodyEncoding( "subprotocolBodyExample" );
      ProtocolInformationSecurityAttributes securityAttributes = new ProtocolInformationSecurityAttributes();
      securityAttributes.setType( ProtocolInformationSecurityAttributes.TypeEnum.NONE );
      securityAttributes.setKey( "Security Attribute key" );
      securityAttributes.setValue( "Security Attribute value" );
      protocolInformation.setSecurityAttributes( List.of( securityAttributes ) );

      Endpoint endpoint = new Endpoint();
      endpoint.setInterface( "interfaceNameExample" );
      endpoint.setProtocolInformation( protocolInformation );

      Reference submodelSemanticReference = new Reference();
      submodelSemanticReference.setType( ReferenceTypes.EXTERNALREFERENCE );
      Key key = new Key();
      key.setType( KeyTypes.SUBMODEL );
      key.setValue( semanticId );
      submodelSemanticReference.setKeys( List.of( key ) );

      Reference submodelSupplemSemanticIdReference = new Reference();
      submodelSupplemSemanticIdReference.setType( ReferenceTypes.EXTERNALREFERENCE );
      Key submodelSupplemSemanticIdkey = new Key();
      submodelSupplemSemanticIdkey.setType( KeyTypes.SUBMODEL );
      submodelSupplemSemanticIdkey.setValue( "supplementalsemanticIdExample value" );
      submodelSupplemSemanticIdReference.setKeys( List.of( submodelSupplemSemanticIdkey ) );

      SubmodelDescriptor submodelDescriptor = new SubmodelDescriptor();
      submodelDescriptor.setId( UUID.randomUUID().toString() );
      submodelDescriptor.setDisplayName( generateDisplayNames() );
      submodelDescriptor.setIdShort( RandomStringUtils.random( 10, true, true ) );
      submodelDescriptor.setSemanticId( submodelSemanticReference );
      submodelDescriptor.setSupplementalSemanticId( List.of( submodelSupplemSemanticIdReference ) );
      submodelDescriptor.setDescription( generateDescriptions() );
      submodelDescriptor.setEndpoints( List.of( endpoint ) );
      return submodelDescriptor;
   }
}
