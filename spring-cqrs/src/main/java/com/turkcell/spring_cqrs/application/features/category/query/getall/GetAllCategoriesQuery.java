package com.turkcell.spring_cqrs.application.features.category.query.getall;

import org.springframework.data.domain.Page;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Query;
import com.turkcell.spring_cqrs.core.security.authorization.AuthorizableRequest;

public record GetAllCategoriesQuery(int pageNumber, int pageSize) implements Query<Page<GetAllCategoriesResponse>>, AuthorizableRequest {}


// JWT loginden alınır
// JWTsiz bi şekilde category GET isteği hata vermeli (RuntimeException)
// JWT "Authorization" Bearer {jwt} eklenirse
// sonuç gelmeli..


// 1- JWT yapısına ve UserContext yapısına rol mantığı eklenecek. (Her request dilerse rol gerektirebilir -> Role listesi dooldurulursa rolelerde kontrol edilmeli)
// 2- Auth hataları kendine has exceptionlar fırlatmalı. (AuthenticatedException,AuthorizationException)
// 3- Custom exceptionlar custom handle edilip 401-403 dönmeli.