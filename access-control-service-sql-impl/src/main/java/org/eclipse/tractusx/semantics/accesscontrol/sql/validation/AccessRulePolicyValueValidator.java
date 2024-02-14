/*******************************************************************************
 * Copyright (c) 2024 Robert Bosch Manufacturing Solutions GmbH and others
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
 *
 ******************************************************************************/
package org.eclipse.tractusx.semantics.accesscontrol.sql.validation;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.tractusx.semantics.accesscontrol.sql.model.policy.AccessRulePolicyValue;
import org.springframework.util.CollectionUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccessRulePolicyValueValidator implements ConstraintValidator<ValidAccessRulePolicyValue, AccessRulePolicyValue> {
   private String message;

   @Override
   public void initialize( ValidAccessRulePolicyValue constraintAnnotation ) {
      ConstraintValidator.super.initialize( constraintAnnotation );
      this.message = constraintAnnotation.message();
   }

   @Override
   public boolean isValid( AccessRulePolicyValue accessRulePolicyValue, ConstraintValidatorContext constraintValidatorContext ) {
      var valid = true;
      if ( StringUtils.isBlank( accessRulePolicyValue.attribute() ) ) {
         constraintValidatorContext.buildConstraintViolationWithTemplate( "attribute must not be null or blank." )
               .addPropertyNode( "attribute" ).addConstraintViolation();
         valid = false;
      }
      if ( accessRulePolicyValue.operator() == null ) {
         constraintValidatorContext.buildConstraintViolationWithTemplate( "Operator must not be null." )
               .addPropertyNode( "operator" ).addConstraintViolation();
         valid = false;
      }
      if ( accessRulePolicyValue.hasSingleValue() ) {
         if ( StringUtils.isBlank( accessRulePolicyValue.value() ) ) {
            constraintValidatorContext.buildConstraintViolationWithTemplate( "Value must not be null or blank if the policy hasSingleValue() is true." )
                  .addPropertyNode( "value" ).addConstraintViolation();
            valid = false;
         }
         if ( accessRulePolicyValue.operator() != null && !accessRulePolicyValue.operator().isSingleValued() ) {
            constraintValidatorContext.buildConstraintViolationWithTemplate( "Operator must be single valued if the policy hasSingleValue() is true." )
                  .addPropertyNode( "operator" ).addConstraintViolation();
            valid = false;
         }
      } else {
         if ( CollectionUtils.isEmpty( accessRulePolicyValue.values() ) ) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Values must not be null or empty collection if the policy hasSingleValue() is false." )
                  .addPropertyNode( "values" ).addConstraintViolation();
            valid = false;
         }
         if ( accessRulePolicyValue.values().stream().anyMatch( Objects::isNull ) ) {
            constraintValidatorContext.buildConstraintViolationWithTemplate( "Values cannot contain null entries if the policy hasSingleValue() is false." )
                  .addPropertyNode( "values" ).addConstraintViolation();
            valid = false;
         }
         if ( accessRulePolicyValue.operator() != null && accessRulePolicyValue.operator().isSingleValued() ) {
            constraintValidatorContext.buildConstraintViolationWithTemplate( "Operator must not be single valued if the policy hasSingleValue() is false." )
                  .addPropertyNode( "operator" ).addConstraintViolation();
            valid = false;
         }
      }
      if ( !valid ) {
         constraintValidatorContext.buildConstraintViolationWithTemplate( message ).addConstraintViolation();
      }
      return valid;
   }
}
