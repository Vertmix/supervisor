package com.vertmix.supervisor.instance.controller;

import com.vertmix.supervisor.instance.service.InstanceService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstanceController<T> {

    private final InstanceService<T> instanceService;



}
