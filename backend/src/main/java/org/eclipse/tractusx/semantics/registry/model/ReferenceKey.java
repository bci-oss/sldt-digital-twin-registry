package org.eclipse.tractusx.semantics.registry.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.Value;

@Value
public class ReferenceKey {

   @Id
   UUID id;
   private ReferenceKeyType type;
   private String value;

}