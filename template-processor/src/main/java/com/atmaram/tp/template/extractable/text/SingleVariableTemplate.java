package com.atmaram.tp.template.extractable.text;

import com.atmaram.tp.common.ExpressionProcessor;
import com.atmaram.tp.common.ExpressionTree;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.Template;
import com.atmaram.tp.template.extractable.ExtractableTemplate;

public interface SingleVariableTemplate extends ExtractableTemplate {
    public static SingleVariableTemplate parse(String template) throws TemplateParseException {
        String trimmed=template.trim();
        if(Template.isVariable(trimmed)){
            ExpressionTree tree= ExpressionProcessor.toTree(Template.getVariableName(trimmed));
            if(tree.getVariable()!=null){
                return new SingleVariableStringTemplate(tree.getVariable());
            }
        }
        throw new TemplateParseException("Provided Template is not valid Single Variable Template"+template);
    }
}
