package in.virit;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.Consumer;

// Instucts Vaadin tooling to include this JS dependency
@NpmPackage(value = "nouislider", version = "15.8.1")
// This tiny JS exposes methods from JS module to the element/host page
@JsModule("./nouislider/connector.js")
// This tag name is just "informational", no real web component, just custom tag name
@Tag("noui-slider")
public class NoUiSlider extends Component implements HasSize {

    public record Range(double min, double max) {
    }

    private  double value = 0;

    private Range range = new Range(0, 100);

    private Consumer<Double> listener;

    public NoUiSlider(Consumer<Double> listener) {
        this.listener = listener;
        setMinWidth("200px");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getElement().executeJs("""
                const el = this;
                el._slider = _createSlider(this, {
                    start: $0,
                    connect: [true, false],
                    range: $1
                });
                el._slider.on("update", () => {
                    const value = el._slider.get();
                    // pass to the exposed "server method"
                    el.$server.receiveValue(value);
                });
                """, value, range);
    }

    @ClientCallable
    void receiveValue(double d) {
        value = d;
        listener.accept(value);
    }

    public void setRange(Range range) {
        assert isAttached() == false;
        this.range = range;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        if(isAttached()) {
            getElement().executeJs("this._slider.set($0)", value);
        }
    }

}