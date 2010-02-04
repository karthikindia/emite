package com.calclab.hablar.basic.client.ui.pages;

import java.util.List;

import com.calclab.hablar.basic.client.HablarEventBus;
import com.calclab.hablar.basic.client.ui.page.PageView;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PagesWidget extends Composite implements Pages {
    private final PagesLogic logic;

    public PagesWidget(HablarEventBus hablarEventBus, PagesPanel panel) {
	logic = new PagesLogic(hablarEventBus, panel);
	initWidget((Widget) panel);
    }

    @Override
    public void add(PageView pageView) {
	logic.add(pageView);
    }

    @Override
    public void addGhost(PageView pageView) {
	logic.addGhost(pageView);
    }

    @Override
    public boolean close(PageView pageView) {
	return logic.close(pageView);
    }

    @Override
    public List<PageView> getPagesOfType(String pageType) {
	return logic.getPagesOfType(pageType);
    }

    @Override
    public boolean hasPageView(PageView pageView) {
	return logic.hasPageView(pageView);
    }

    @Override
    public void hide(PageView pageView) {
	logic.hide(pageView);
    }

    @Override
    public void open(PageView pageView) {
	logic.open(pageView);
    }

}
