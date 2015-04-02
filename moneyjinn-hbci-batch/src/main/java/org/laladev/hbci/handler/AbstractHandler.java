package org.laladev.hbci.handler;

import java.util.Observable;

import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.structures.Konto;

public abstract class AbstractHandler extends Observable {

	public abstract void handle(final HBCIHandler hbciHandle, final Konto account);

}
