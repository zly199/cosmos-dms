package top.aotm.cosmosdms.editor.contributor;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class CosmosCompletionContributor extends CompletionContributor {
    public CosmosCompletionContributor() {
        extend(CompletionType.BASIC, 
               PlatformPatterns.psiElement(), 
               new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       @NotNull ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                resultSet.addElement(LookupElementBuilder.create("SELECT"));
                resultSet.addElement(LookupElementBuilder.create("FROM"));
                // 更多的 SQL 关键字
            }
        });
    }
}
