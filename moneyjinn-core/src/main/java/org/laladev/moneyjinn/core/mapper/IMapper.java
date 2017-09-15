package org.laladev.moneyjinn.core.mapper;

public interface IMapper<A, B> {
	A mapBToA(B b);

	B mapAToB(A a);
}
