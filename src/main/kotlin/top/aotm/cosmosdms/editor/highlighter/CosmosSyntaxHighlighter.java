package top.aotm.cosmosdms.editor.highlighter;

/**
 * @author: zenglingyog.zly
 * @version: 1.0
 * @createAt: 2023/12/25
 * @description:
 */
import com.intellij.lexer.EmptyLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class CosmosSyntaxHighlighter extends SyntaxHighlighterBase {

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        // 返回您的 Lexer，用于分解文本为词法单元
        return new EmptyLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        // 根据词法单元类型返回对应的高亮样式
//        if (tokenType.equals(CosmSqlTokenTypes.KEYWORD)) {
//            return pack(DefaultLanguageHighlighterColors.KEYWORD);
//        }
        // 其他词法单元的高亮
        return EMPTY;
    }
}
