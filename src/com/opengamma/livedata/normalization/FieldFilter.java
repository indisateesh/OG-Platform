/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.normalization;

import java.util.Collection;
import java.util.HashSet;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeField;
import org.fudgemsg.MutableFudgeFieldContainer;

import com.opengamma.livedata.server.FieldHistoryStore;
import com.opengamma.util.ArgumentChecker;

/**
 * 
 *
 * @author pietari
 */
public class FieldFilter implements NormalizationRule {
  
  private final Collection<String> _fieldsToAccept;
  private final FudgeContext CONTEXT = FudgeContext.GLOBAL_DEFAULT; 
  
  public FieldFilter(Collection<String> fieldsToAccept) {
    ArgumentChecker.checkNotNull(fieldsToAccept, "List of accepted fields");    
    _fieldsToAccept = new HashSet<String>(fieldsToAccept);
  }

  @Override
  public MutableFudgeFieldContainer apply(
      MutableFudgeFieldContainer msg,
      FieldHistoryStore fieldHistory) {
    
    MutableFudgeFieldContainer normalizedMsg = CONTEXT.newMessage();
    // REVIEW kirk 2010-04-15 -- Run through the fields in the order of the
    // original message and check for containment in _fieldsToAccept as it's
    // faster for large messages.
    // It also supports multiple values with the same name.
    for (FudgeField field: msg.getAllFields()) {
      if(field.getName() == null) {
        // Don't allow non-named fields.
        continue;
      }
      if(!_fieldsToAccept.contains(field.getName())) {
        continue;
      }
      normalizedMsg.add(field);
    }
    return normalizedMsg;
    
  }
  
}
