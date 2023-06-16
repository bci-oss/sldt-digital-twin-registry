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
package org.eclipse.tractusx.semantics.registry.controller;

import java.util.Base64;
import java.util.Optional;

import org.eclipse.tractusx.semantics.aas.registry.api.DescriptionApiDelegate;
import org.eclipse.tractusx.semantics.aas.registry.api.ShellDescriptorsApiDelegate;
import org.eclipse.tractusx.semantics.aas.registry.model.AssetAdministrationShellDescriptor;
import org.eclipse.tractusx.semantics.aas.registry.model.AssetKind;
import org.eclipse.tractusx.semantics.aas.registry.model.GetAssetAdministrationShellDescriptorsResult;
import org.eclipse.tractusx.semantics.aas.registry.model.GetSubmodelDescriptorsResult;
import org.eclipse.tractusx.semantics.aas.registry.model.ServiceDescription;
import org.eclipse.tractusx.semantics.aas.registry.model.SubmodelDescriptor;
import org.eclipse.tractusx.semantics.registry.dto.ShellCollectionDto;
import org.eclipse.tractusx.semantics.registry.mapper.ShellMapper;
import org.eclipse.tractusx.semantics.registry.mapper.SubmodelMapper;
import org.eclipse.tractusx.semantics.registry.model.Shell;
import org.eclipse.tractusx.semantics.registry.model.Submodel;
import org.eclipse.tractusx.semantics.registry.service.ShellService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

@Service
public class AssetAdministrationShellApiDelegateNEW implements DescriptionApiDelegate, ShellDescriptorsApiDelegate {

    private final ShellService shellService;
    private final ShellMapper shellMapper;
    private final SubmodelMapper submodelMapper;

    public AssetAdministrationShellApiDelegateNEW(final ShellService shellService,
                                               final ShellMapper shellMapper,
                                               final SubmodelMapper submodelMapper) {
        this.shellService = shellService;
        this.shellMapper = shellMapper;
        this.submodelMapper = submodelMapper;
    }


    //DescriptionApiDelegate
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return DescriptionApiDelegate.super.getRequest();
    }

    @Override
    public ResponseEntity<ServiceDescription> getDescription() {
        return DescriptionApiDelegate.super.getDescription();
    }

    // ShellDescriptorsApiDelegate

    @Override
    public ResponseEntity<Void> deleteAssetAdministrationShellDescriptorById( byte[] aasIdentifier ) {
        String aasIdentifierID = Base64.getUrlEncoder().encodeToString( aasIdentifier );
        shellService.deleteShell( aasIdentifierID );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    // new
    public ResponseEntity<Void> deleteSubmodelDescriptorByIdThroughSuperpath( byte[] aasIdentifier, byte[] submodelIdentifier ) {
        String aasIdentifierID = Base64.getUrlEncoder().encodeToString( aasIdentifier );
        String submodelIdentifierID = Base64.getUrlEncoder().encodeToString( submodelIdentifier );

        shellService.deleteSubmodel(aasIdentifierID, submodelIdentifierID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        //return ShellDescriptorsApiDelegate.super.deleteSubmodelDescriptorByIdThroughSuperpath( aasIdentifier, submodelIdentifier );
    }

    @Override
    public ResponseEntity<GetAssetAdministrationShellDescriptorsResult> getAllAssetAdministrationShellDescriptors( Integer limit, String cursor,
          AssetKind assetKind, String assetType ) {
        Integer page = 0 ;
        Integer pageSize = 10;

        return new ResponseEntity<>(shellMapper.toApiDto(shellService.findAllShells(page, pageSize)), HttpStatus.OK);
       // return ShellDescriptorsApiDelegate.super.getAllAssetAdministrationShellDescriptors( limit, cursor, assetKind, assetType );
    }

    @Override
    // new
    public ResponseEntity<GetSubmodelDescriptorsResult> getAllSubmodelDescriptorsThroughSuperpath( byte[] aasIdentifier, Integer limit, String cursor ) {

       // return not implemented

        return ShellDescriptorsApiDelegate.super.getAllSubmodelDescriptorsThroughSuperpath( aasIdentifier, limit, cursor );
    }

    @Override
    public ResponseEntity<AssetAdministrationShellDescriptor> getAssetAdministrationShellDescriptorById( byte[] aasIdentifier ) {
        String aasIdentifierID = Base64.getUrlEncoder().encodeToString( aasIdentifier );
        Shell saved = shellService.findShellByExternalId( aasIdentifierID );
        return new ResponseEntity<>(shellMapper.toApiDto(saved), HttpStatus.OK);
    }

    @Override
    // new
    public ResponseEntity<SubmodelDescriptor> getSubmodelDescriptorByIdThroughSuperpath( byte[] aasIdentifier, byte[] submodelIdentifier ) {
        String assIdentifierID = Base64.getUrlEncoder().encodeToString( aasIdentifier );
        String submodelIdenifierID = Base64.getUrlEncoder().encodeToString( submodelIdentifier );
        Submodel submodel = shellService.findSubmodelByExternalId(assIdentifierID, submodelIdenifierID);
        return new ResponseEntity<>(submodelMapper.toApiDto(submodel), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssetAdministrationShellDescriptor> postAssetAdministrationShellDescriptor( AssetAdministrationShellDescriptor assetAdministrationShellDescriptor ) {
        Shell saved = shellService.save(shellMapper.fromApiDto(assetAdministrationShellDescriptor));
        return new ResponseEntity<>(shellMapper.toApiDto(saved), HttpStatus.CREATED);
    }

    @Override
    // new
    public ResponseEntity<SubmodelDescriptor> postSubmodelDescriptorThroughSuperpath( byte[] aasIdentifier, SubmodelDescriptor submodelDescriptor ) {
        String assIdentifierID = Base64.getUrlEncoder().encodeToString( aasIdentifier );
        Submodel toBeSaved = submodelMapper.fromApiDto(submodelDescriptor);
        Submodel savedSubModel = shellService.save(assIdentifierID, toBeSaved);
        return new ResponseEntity<>(submodelMapper.toApiDto(savedSubModel), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> putAssetAdministrationShellDescriptorById( byte[] aasIdentifier, AssetAdministrationShellDescriptor assetAdministrationShellDescriptor ) {
        String aasIdentifierID = Base64.getUrlEncoder().encodeToString( aasIdentifier );
        Shell shell = shellMapper.fromApiDto( assetAdministrationShellDescriptor );
        shellService.update( aasIdentifierID, shell.withIdExternal( aasIdentifierID ) );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    // new
    public ResponseEntity<Void> putSubmodelDescriptorByIdThroughSuperpath( byte[] aasIdentifier, byte[] submodelIdentifier, SubmodelDescriptor submodelDescriptor ) {
        String aasIdentifierID = Base64.getUrlEncoder().encodeToString( aasIdentifier );
        String submodelIdentifierID =  Base64.getUrlEncoder().encodeToString( submodelIdentifier );
        Submodel submodel = submodelMapper.fromApiDto( submodelDescriptor );
        shellService.update( aasIdentifierID, submodelIdentifierID, submodel.withIdExternal( submodelIdentifierID ) );
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );

    }



}

