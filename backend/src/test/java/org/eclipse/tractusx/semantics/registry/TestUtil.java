package org.eclipse.tractusx.semantics.registry;

import org.eclipse.tractusx.semantics.aas.registry.model.*;

import java.util.List;
import java.util.UUID;

public class TestUtil {

    public static AssetAdministrationShellDescriptor createCompleteAasDescriptor() {
        AssetAdministrationShellDescriptor aas = new AssetAdministrationShellDescriptor();

        LangStringNameType displayName = new LangStringNameType();
        displayName.setLanguage("de");
        displayName.setText("this is an example description1");
        aas.setDisplayName(List.of(displayName));


        aas.setId("fb7ebcc2-5731-4948-aeaa-c9e9692decf5");
        //aas.setIdentification("identificationExample");
        aas.setIdShort("idShortExample");

        String globalAssetID = "globalAssetIdExample";
        aas.setGlobalAssetId(globalAssetID);


        SpecificAssetId specificAssetId1 = new SpecificAssetId();
        specificAssetId1.setName("identifier1KeyExample");
        specificAssetId1.setValue("identifier1ValueExample");


        SpecificAssetId specificAssetId2 = new SpecificAssetId();
        specificAssetId2.setName("identifier2KeyExample");
        specificAssetId2.setValue("identifier2ValueExample");

        aas.setSpecificAssetIds(List.of(specificAssetId1, specificAssetId2));

//        IdentifierKeyValuePair identifier1 = new IdentifierKeyValuePair();
//        identifier1.setKey("identifier1KeyExample");
//        identifier1.setValue("identifier1ValueExample");
//
//        IdentifierKeyValuePair identifier2 = new IdentifierKeyValuePair();
//        identifier2.setKey("identifier2KeyExample");
//        identifier2.setValue("identifier2ValueExample");
//        aas.setSpecificAssetIds(List.of(identifier1, identifier2));

        LangStringTextType description1 = new LangStringTextType();
        description1.setLanguage("de");
        description1.setText("hello text");
        LangStringTextType description2 = new LangStringTextType();
        description2.setLanguage("en");
        description2.setText("hello s");
        aas.setDescription(List.of(description1, description2));

//        LangString description1 = new LangString();
//        description1.setLanguage("de");
//        description1.setText("this is an example description1");
//
//        LangString description2 = new LangString();
//        description2.setLanguage("en");
//        description2.setText("this is an example for description2");
        aas.setDescription(List.of(description1, description2));


        ProtocolInformation protocolInformation = new ProtocolInformation();
        protocolInformation.setEndpointProtocol("endpointProtocolExample");

        // protocolInformation.setEndpointAddress("endpointAddressExample");
        protocolInformation.setHref("endpointAddressExample");

        //protocolInformation.setEndpointProtocolVersion("endpointProtocolVersionExample");
        protocolInformation.setEndpointProtocolVersion(List.of("e"));

        protocolInformation.setSubprotocol("subprotocolExample");
        protocolInformation.setSubprotocolBody("subprotocolBodyExample");
        protocolInformation.setSubprotocolBodyEncoding("subprotocolBodyExample");

        ProtocolInformationSecurityAttributes securityAttributes = new ProtocolInformationSecurityAttributes();
        securityAttributes.setType(ProtocolInformationSecurityAttributes.TypeEnum.NONE);
        protocolInformation.setSecurityAttributes(List.of(securityAttributes));


        Endpoint endpoint = new Endpoint();
        endpoint.setInterface("interfaceNameExample");
        endpoint.setProtocolInformation(protocolInformation);

        Reference reference = new Reference();
        reference.setType(ReferenceTypes.EXTERNALREFERENCE);
        Key key = new Key();
        key.setType(KeyTypes.SUBMODEL);
        key.setValue("semanticIdExample");
        reference.setKeys(List.of(key));
        //reference.setValue(List.of("semanticIdExample"));

        Extension extension = new Extension();
        extension.setRefersTo(List.of(reference));
        extension.addSupplementalSemanticIdsItem(reference);

        aas.setExtensions(List.of(extension));

        SubmodelDescriptor submodelDescriptor = new SubmodelDescriptor();

        //submodelDescriptor.setIdentification("identificationExample");
        submodelDescriptor.setId(UUID.randomUUID().toString());

        submodelDescriptor.setIdShort("idShortExample");
        submodelDescriptor.setSemanticId(reference);
        specificAssetId1.setSupplementalSemanticIds(List.of(reference));
        specificAssetId2.setSupplementalSemanticIds(List.of(reference));

        submodelDescriptor.setDescription(List.of(description1, description2));
        submodelDescriptor.setEndpoints(List.of(endpoint));
        aas.setEndpoints(List.of(endpoint));
        aas.setSubmodelDescriptors(List.of(submodelDescriptor));

        return aas;
    }

    public static SubmodelDescriptor createSubmodel(){
        SubmodelDescriptor submodelDescriptor = new SubmodelDescriptor();

        //submodelDescriptor.setIdentification("identificationExample");
        submodelDescriptor.setId(UUID.randomUUID().toString());
        Reference reference = new Reference();
        reference.setType(ReferenceTypes.EXTERNALREFERENCE);
        Key key = new Key();
        key.setType(KeyTypes.SUBMODEL);
        key.setValue("semanticIdExample");
        reference.setKeys(List.of(key));
        submodelDescriptor.setIdShort("idShortExample");
        submodelDescriptor.setSemanticId(reference);
        LangStringTextType description1 = new LangStringTextType();
        description1.setLanguage("de");
        description1.setText("hello text");
        LangStringTextType description2 = new LangStringTextType();
        description2.setLanguage("en");
        description2.setText("hello s");

        ProtocolInformation protocolInformation = new ProtocolInformation();
        protocolInformation.setEndpointProtocol("endpointProtocolExample");

        // protocolInformation.setEndpointAddress("endpointAddressExample");
        protocolInformation.setHref("endpointAddressExample");

        //protocolInformation.setEndpointProtocolVersion("endpointProtocolVersionExample");
        protocolInformation.setEndpointProtocolVersion(List.of("e"));

        protocolInformation.setSubprotocol("subprotocolExample");
        protocolInformation.setSubprotocolBody("subprotocolBodyExample");
        protocolInformation.setSubprotocolBodyEncoding("subprotocolBodyExample");



        Endpoint endpoint = new Endpoint();
        endpoint.setInterface("interfaceNameExample");
        endpoint.setProtocolInformation(protocolInformation);

        submodelDescriptor.setDescription(List.of(description1, description2));
        submodelDescriptor.setEndpoints(List.of(endpoint));

        return submodelDescriptor;
    }

    public static SpecificAssetId createSpecificAssetId(){
        SpecificAssetId specificAssetId1 = new SpecificAssetId();
        specificAssetId1.setName("identifier1KeyExample");
        specificAssetId1.setValue("identifier1ValueExample");


        SpecificAssetId specificAssetId2 = new SpecificAssetId();
        specificAssetId2.setName("identifier2KeyExample");
        specificAssetId2.setValue("identifier2ValueExample");

        Reference reference = new Reference();
        reference.setType(ReferenceTypes.EXTERNALREFERENCE);
        Key key = new Key();
        key.setType(KeyTypes.SUBMODEL);
        key.setValue("key");
        reference.setKeys(List.of(key));

        specificAssetId1.setSupplementalSemanticIds(List.of(reference));
        specificAssetId2.setSupplementalSemanticIds(List.of(reference));

        return specificAssetId1;
      // return List.of(specificAssetId1, specificAssetId2);


    }

}
