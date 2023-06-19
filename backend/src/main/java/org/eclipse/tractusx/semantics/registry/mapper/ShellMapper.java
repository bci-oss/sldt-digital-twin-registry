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

import org.eclipse.tractusx.semantics.aas.registry.model.AssetAdministrationShellDescriptor;
import org.eclipse.tractusx.semantics.aas.registry.model.GetAssetAdministrationShellDescriptorsResult;
import org.eclipse.tractusx.semantics.aas.registry.model.LangStringTextType;
import org.eclipse.tractusx.semantics.aas.registry.model.Reference;
import org.eclipse.tractusx.semantics.aas.registry.model.SpecificAssetId;
import org.eclipse.tractusx.semantics.registry.dto.ShellCollectionDto;
import org.eclipse.tractusx.semantics.registry.model.Shell;
import org.eclipse.tractusx.semantics.registry.model.ShellDescription;
import org.eclipse.tractusx.semantics.registry.model.ShellIdentifier;
import org.mapstruct.*;


@Mapper(uses = {SubmodelMapper.class}, componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface ShellMapper {
    @Mappings({
          @Mapping(target = "idExternal", source = "id"),
          @Mapping(target = "identifiers", source = "specificAssetIds"),
         @Mapping(target = "descriptions", source = "description"),
          @Mapping(target = "submodels", source = "submodelDescriptors"),
           //@Mapping(target = "submodels.descriptions.text", source = "description.text"),
          @Mapping(target = "id", ignore = true)
    })
    Shell fromApiDto(AssetAdministrationShellDescriptor apiDto);

   //@Mapping(target = "text", ignore = true)
    ShellDescription mapShellDescription (LangStringTextType description);

   @Mappings({
         @Mapping(target = "key", source = "name"),
         @Mapping(target = "value", source = "value"),
         @Mapping(target = "externalSubjectId", source = "externalSubjectId")
   })
    ShellIdentifier mapSpecificAssetID(SpecificAssetId specificAssetId);



//    List<Shell> fromListApiDto(List<AssetAdministrationShellDescriptor> apiDto); -> Batch
/*
    ShellIdentifier fromApiDto(IdentifierKeyValuePair apiDto); -> specificAssetId

    Set<ShellIdentifier> fromApiDto(List<IdentifierKeyValuePair> apiDto); -> specificAssetIds

    AssetAdministrationShellDescriptorCollection toApiDto( ShellCollectionDto shell); -> Collection
/*
    @Mappings({
            @Mapping(target = "identification", source = "idExternal"),
    })
    BatchResult toApiDto( BatchResultDto batchResult);

    List<BatchResult> toListApiDto(List<BatchResultDto> batchResults);
*/

    @Mappings({
            @Mapping(source = "idExternal", target = "id"),
            @Mapping(source = "identifiers", target = "specificAssetIds"),
            @Mapping(source = "descriptions", target = "description"),
            @Mapping(source = "submodels", target = "submodelDescriptors"),
    })
    @InheritInverseConfiguration
    AssetAdministrationShellDescriptor toApiDto(Shell shell);

  // @Mapping(target = "text", ignore = true)
   LangStringTextType mapAssetDescription (ShellDescription description);


   GetAssetAdministrationShellDescriptorsResult toApiDto( ShellCollectionDto shell);

/*
    List<AssetAdministrationShellDescriptor> toApiDto(List<Shell> shell); -> Batch

    List<IdentifierKeyValuePair> toApiDto(Set<ShellIdentifier> shell); -> specificAssetID

    @AfterMapping
    default Shell convertGlobalAssetIdToShellIdentifier(AssetAdministrationShellDescriptor apiDto, @MappingTarget Shell shell){
        return ShellMapperCustomization.globalAssetIdToShellIdentifier(apiDto, shell);
    }

    @AfterMapping
    default void convertShellIdentifierToGlobalAssetId(Shell shell, @MappingTarget AssetAdministrationShellDescriptor apiDto){
        ShellMapperCustomization.shellIdentifierToGlobalAssetId(shell, apiDto);
    }

    @AfterMapping
    default void removeGlobalAssetIdFromIdentifiers(@MappingTarget List<IdentifierKeyValuePair> apiDto){
        ShellMapperCustomization.removeGlobalAssetIdIdentifier(apiDto);
    }
*/
}
