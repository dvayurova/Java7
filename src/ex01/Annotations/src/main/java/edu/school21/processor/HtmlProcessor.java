package edu.school21.processor;

import edu.school21.annotations.HtmlForm;
import edu.school21.annotations.HtmlInput;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

@AutoService(HtmlProcessor.class)
public class HtmlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                try {
                    generateHtmlFile(element);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    private void generateHtmlFile(Element classElement) throws IOException {
        HtmlForm htmlForm = classElement.getAnnotation(HtmlForm.class);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("target/classes/" + htmlForm.fileName()))) {
            writer.write("<form ");
            writer.write("action='" + htmlForm.action() + "' ");
            writer.write("method='" + htmlForm.method() + "'>\n");

            for (Element enclosedElement : classElement.getEnclosedElements()) {
                if (enclosedElement.getKind() == ElementKind.FIELD && enclosedElement.getAnnotation(HtmlInput.class) != null) {
                    HtmlInput htmlInput = enclosedElement.getAnnotation(HtmlInput.class);
                    writer.write("\t<input type='" + htmlInput.type() + "' ");
                    writer.write("name='" + htmlInput.name() + "' ");
                    writer.write("placeholder='" + htmlInput.placeholder() + "'>\n");
                }
            }
            writer.write("\t<input type='submit' value='Send'>\n");
            writer.write("</form>\n");
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(HtmlForm.class.getCanonicalName(), HtmlInput.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}