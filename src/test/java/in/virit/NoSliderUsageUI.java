package in.virit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Route
public class NoSliderUsageUI extends VerticalLayout {

    public NoSliderUsageUI() {
        NoUiSlider noUiSlider = new NoUiSlider(value -> {
            add(new Paragraph("Value now " + value));
        });
        add(noUiSlider);


        add(new Button("Set to 17", e-> {
            noUiSlider.setValue(17);
        }));
    }

}
