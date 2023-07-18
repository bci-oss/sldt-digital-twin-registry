/********************************************************************************
 * Copyright (c) 2021-2023 Robert Bosch Manufacturing Solutions GmbH
 * Copyright (c) 2021-2023 Contributors to the Eclipse Foundation
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

import java.util.List;
import java.util.Set;

import org.eclipse.tractusx.semantics.aas.registry.model.AssetAdministrationShellDescriptor;
import org.eclipse.tractusx.semantics.aas.registry.model.Extension;
import org.eclipse.tractusx.semantics.aas.registry.model.GetAssetAdministrationShellDescriptorsResult;
import org.eclipse.tractusx.semantics.aas.registry.model.LangStringTextType;
import org.eclipse.tractusx.semantics.aas.registry.model.Reference;
import org.eclipse.tractusx.semantics.aas.registry.model.SpecificAssetId;
import org.eclipse.tractusx.semantics.registry.dto.ShellCollectionDto;
import org.eclipse.tractusx.semantics.registry.model.Shell;
import org.eclipse.tractusx.semantics.registry.model.ShellDescription;
import org.eclipse.tractusx.semantics.registry.model.ShellExtension;
import org.eclipse.tractusx.semantics.registry.model.ShellExtensionRefersToReference;
import org.eclipse.tractusx.semantics.registry.model.ShellExtensionSemanticIdReference;
import org.eclipse.tractusx.semantics.registry.model.ShellExtensionSupplemSemanticIdReference;
import org.eclipse.tractusx.semantics.registry.model.ShellIdentifier;
import org.eclipse.tractusx.semantics.registry.model.ShellIdentifierExternalSubjectReference;
import org.eclipse.tractusx.semantics.registry.model.ShellIdentifierSemanticReference;
import org.eclipse.tractusx.semantics.registry.model.ShellIdentifierSupplemSemanticReference;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(uses = {SubmodelMapper.class}, componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface ShellMapper {
    @Mappings({
          @Mapping(target = "idExternal", source = "id"),
          @Mapping(target = "identifiers", source = "specificAssetIds"),
          @Mapping(target = "descriptions", source = "description"),
          @Mapping(target = "submodels", source = "submodelDescriptors"),
          @Mapping(target = "shellType", source = "assetType"),
          @Mapping(target = "shellKind", source = "assetKind"),
          @Mapping(target = "id", ignore = true),
          @Mapping(target = "displayNames", source = "displayName"),
          @Mapping(target = "shellExtensions", source = "extensions"),

    })
    Shell fromApiDto(AssetAdministrationShellDescriptor apiDto);

    ShellDescription mapShellDescription (LangStringTextType description);

    @Mappings({
          @Mapping(target = "key", source = "name"),
          @Mapping(target = "supplementalSemanticIds", source = "supplementalSemanticIds"),
          @Mapping(target = "semanticId", source = "semanticId"),
          @Mapping(target = "externalSubjectId", source = "externalSubjectId"),
    })
    ShellIdentifier fromApiDto(SpecificAssetId apiDto);

   ShellIdentifierSupplemSemanticReference maptoShellIdentifierSupplemSemanticReference ( Reference supplementalSemanticId );

   ShellIdentifierSemanticReference maptoShellIdentifierSemanticReference ( Reference semanticId );

   ShellIdentifierExternalSubjectReference maptoShellIdentifierExternalSubjectReference ( Reference externalSubjectId );

   @Mappings({
         @Mapping(target = "supplementalSemanticIds", source = "supplementalSemanticIds"),
         @Mapping(target = "semanticId", source = "semanticId"),
         @Mapping(target = "refersTo", source = "refersTo"),
   })
   ShellExtension fromApiDto( Extension apiDto);

   ShellExtensionSupplemSemanticIdReference maptoShellExtensionSupplemSemanticReference ( Reference supplementalSemanticId );

   ShellExtensionSemanticIdReference maptoShellExtensionSemanticReference ( Reference semanticId );

   ShellExtensionRefersToReference maptoShellExtensionRefersToReference ( Reference refersTo );


    Set<ShellIdentifier> fromApiDto(List<SpecificAssetId> apiDto);

    @Mappings({
          @Mapping(target = "name", source = "key"),
    })
    SpecificAssetId fromDtoApi(ShellIdentifier apiDto);

    @Mappings({
         @Mapping(source = "idExternal", target = "id"),
         @Mapping(source = "identifiers", target = "specificAssetIds"),
         @Mapping(source = "descriptions", target = "description"),
         @Mapping(source = "submodels", target = "submodelDescriptors"),
          @Mapping(source = "displayNames", target = "displayName"),
          @Mapping(source = "shellExtensions", target = "extensions"),
    })
    @InheritInverseConfiguration
    AssetAdministrationShellDescriptor toApiDto(Shell shell);

   LangStringTextType mapAssetDescription (ShellDescription description);


    @Mappings({
         @Mapping(source = "items", target = "result"),
    })
   GetAssetAdministrationShellDescriptorsResult toApiDto( ShellCollectionDto shell);

   List<SpecificAssetId> toApiDto(Set<ShellIdentifier> shell);

    @AfterMapping
    default Shell convertGlobalAssetIdToShellIdentifier(AssetAdministrationShellDescriptor apiDto, @MappingTarget Shell shell){
        return ShellMapperCustomization.globalAssetIdToShellIdentifier(apiDto, shell);
    }

    @AfterMapping
    default void convertShellIdentifierToGlobalAssetId(Shell shell, @MappingTarget AssetAdministrationShellDescriptor apiDto){
        ShellMapperCustomization.shellIdentifierToGlobalAssetId(shell, apiDto);
    }

   @AfterMapping
   default void removeGlobalAssetIdFromIdentifiers(@MappingTarget List<SpecificAssetId> apiDto){
      ShellMapperCustomization.removeGlobalAssetIdIdentifier(apiDto);
   }
}
