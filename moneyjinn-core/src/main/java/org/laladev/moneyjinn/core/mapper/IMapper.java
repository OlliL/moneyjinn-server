package org.laladev.moneyjinn.core.mapper;

public interface IMapper<A, B> {
	public A mapBToA(B b);

	public B mapAToB(A a);
}
