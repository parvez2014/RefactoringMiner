package gr.uom.java.xmi.diff;

import java.util.LinkedHashSet;
import java.util.Set;

import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringType;

import gr.uom.java.xmi.LocationInfo;
import gr.uom.java.xmi.UMLOperation;
import gr.uom.java.xmi.decomposition.AbstractCodeFragment;
import gr.uom.java.xmi.decomposition.AbstractCodeMapping;
import gr.uom.java.xmi.decomposition.OperationInvocation;
import gr.uom.java.xmi.decomposition.UMLOperationBodyMapper;
import gr.uom.java.xmi.decomposition.replacement.Replacement;

public class InlineOperationRefactoring implements Refactoring {
	private UMLOperation inlinedOperation;
	private UMLOperation targetOperationAfterInline;
	private UMLOperation targetOperationBeforeInline;
	private OperationInvocation inlinedOperationInvocation;
	private Set<Replacement> replacements;
	private Set<AbstractCodeFragment> inlinedCodeFragments;
	
	public InlineOperationRefactoring(UMLOperationBodyMapper bodyMapper, UMLOperation targetOperationBeforeInline,
			OperationInvocation operationInvocation) {
		this.inlinedOperation = bodyMapper.getOperation1();
		this.targetOperationAfterInline = bodyMapper.getOperation2();
		this.targetOperationBeforeInline = targetOperationBeforeInline;
		this.inlinedOperationInvocation = operationInvocation;
		this.replacements = bodyMapper.getReplacements();
		this.inlinedCodeFragments = new LinkedHashSet<AbstractCodeFragment>();
		for(AbstractCodeMapping mapping : bodyMapper.getMappings()) {
			this.inlinedCodeFragments.add(mapping.getFragment2());
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName()).append("\t");
		sb.append(inlinedOperation);
		sb.append(" inlined to ");
		sb.append(targetOperationAfterInline);
		sb.append(" in class ");
		sb.append(getClassName());
		return sb.toString();
	}

	private String getClassName() {
		return targetOperationAfterInline.getClassName();
	}

	public String getName() {
		return this.getRefactoringType().getDisplayName();
	}

	public RefactoringType getRefactoringType() {
		return RefactoringType.INLINE_OPERATION;
	}

	public UMLOperation getInlinedOperation() {
		return inlinedOperation;
	}

	public UMLOperation getTargetOperationAfterInline() {
		return targetOperationAfterInline;
	}

	public UMLOperation getTargetOperationBeforeInline() {
		return targetOperationBeforeInline;
	}

	public OperationInvocation getInlinedOperationInvocation() {
		return inlinedOperationInvocation;
	}

	public Set<Replacement> getReplacements() {
		return replacements;
	}

	public Set<AbstractCodeFragment> getInlinedCodeFragments() {
		return inlinedCodeFragments;
	}

	public CodeRange getTargetOperationCodeRangeBeforeInline() {
		return targetOperationBeforeInline.codeRange();
	}

	public CodeRange getTargetOperationCodeRangeAfterInline() {
		return targetOperationAfterInline.codeRange();
	}

	public CodeRange getInlinedOperationCodeRange() {
		return inlinedOperation.codeRange();
	}

	public CodeRange getInlinedCodeRangeInTargetOperation() {
		String filePath = null;
		int minStartLine = 0;
		int maxEndLine = 0;
		int startColumn = 0;
		int endColumn = 0;
		
		for(AbstractCodeFragment fragment : inlinedCodeFragments) {
			LocationInfo info = fragment.getLocationInfo();
			filePath = info.getFilePath();
			if(minStartLine == 0 || info.getStartLine() < minStartLine) {
				minStartLine = info.getStartLine();
				startColumn = info.getStartColumn();
			}
			if(info.getEndLine() > maxEndLine) {
				maxEndLine = info.getEndLine();
				endColumn = info.getEndColumn();
			}
		}
		return new CodeRange(filePath, minStartLine, maxEndLine, startColumn, endColumn);
	}

	public CodeRange getInlinedOperationInvocationCodeRange() {
		LocationInfo info = inlinedOperationInvocation.getLocationInfo();
		return info.codeRange();
	}

}
