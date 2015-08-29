package org.laladev.moneyjinn.api;

public interface IMapper<A, B> {
	public A mapBToA(B b);

	public B mapAToB(A a);
}
