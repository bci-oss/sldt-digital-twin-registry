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
package org.eclipse.tractusx.semantics.registry.mapper;

import org.eclipse.tractusx.semantics.aas.registry.model.Endpoint;
import org.eclipse.tractusx.semantics.aas.registry.model.Key;
import org.eclipse.tractusx.semantics.aas.registry.model.KeyTypes;
import org.eclipse.tractusx.semantics.aas.registry.model.LangStringTextType;
import org.eclipse.tractusx.semantics.aas.registry.model.Reference;
import org.eclipse.tractusx.semantics.aas.registry.model.ReferenceTypes;
import org.eclipse.tractusx.semantics.aas.registry.model.SubmodelDescriptor;
import org.eclipse.tractusx.semantics.registry.model.ShellDescription;
import org.eclipse.tractusx.semantics.registry.model.Submodel;
import org.eclipse.tractusx.semantics.registry.model.SubmodelDescription;
import org.eclipse.tractusx.semantics.registry.model.SubmodelEndpoint;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SubmodelMapper {
    @Mappings({
            @Mapping(target="idExternal", source="id"), // id == identification
          @Mapping(target = "descriptions", source = "description"),
            @Mapping(target="semanticId", source = "semanticId")
    })
    Submodel fromApiDto(SubmodelDescriptor apiDto);

   //@Mapping(target = "text", ignore = true)
   SubmodelDescription mapShellDescription (LangStringTextType description);

    @Mappings({
            @Mapping(target="interfaceName", source = "interface"),
            @Mapping(target="endpointAddress", source = "protocolInformation.href"),   // "href" == "protocolInformation.endpointAddress"
            @Mapping(target="endpointProtocol", source = "protocolInformation.endpointProtocol"),
            @Mapping(target="endpointProtocolVersion", source = "protocolInformation.endpointProtocolVersion"),
            @Mapping(target="subProtocol", source = "protocolInformation.subprotocol"),
            @Mapping(target="subProtocolBody", source = "protocolInformation.subprotocolBody"),
            @Mapping(target="subProtocolBodyEncoding", source = "protocolInformation.subprotocolBodyEncoding"),
    })
    SubmodelEndpoint fromApiDto(Endpoint apiDto);

    //todo: protocolversion -> array to string
   default String protocolVersion(List<String> versions){
      if (versions.size() > 0){
         return versions.get( 0 );
      } else {
         return null;
      }
   }






//    @InheritInverseConfiguration
//    List<SubmodelDescriptor> toApiDto(Set<Submodel> shell);

    @InheritInverseConfiguration
    SubmodelDescriptor toApiDto(Submodel shell);

  // @Mapping(target = "text", ignore = true)
   LangStringTextType mapSubModelDescription (SubmodelDescription description);

    @InheritInverseConfiguration
    Endpoint toApiDto(SubmodelEndpoint apiDto);

   //@Mapping(target = "text", ignore = true)
   default List<String>  protocolVersionDescriptor(String version){
      return List.of(version);
   }


//    default String map(Reference reference){
//
//        return reference != null && reference.getValue() != null && !reference.getValue().isEmpty() ? reference.getValue().get(0) : null;
//    }

   default String map(Reference reference){

      return reference != null && reference.getKeys().get( 0 ) != null && !reference.getKeys().get( 0 ).getValue().isEmpty() ? reference.getKeys().get( 0 ).getValue() : null;
   }


   // todo: implement types
    default Reference map(String semanticId){
        if(semanticId == null ||  semanticId.isBlank()) {
            return null;
        }
        Reference reference = new Reference();
        reference.setType( ReferenceTypes.EXTERNALREFERENCE );
        Key key = new Key();
        key.setValue( semanticId );
        //reference.setValue(List.of(semanticId));
        return reference;
    }



}
