package com.nbu.controlnode.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nbu.controlnode.datanode.DataNode;

@Service
public class RehashingService {

    public void rehashKeys(DataNode dataNodeWithKeysToRehash) {
        if (dataNodeWithKeysToRehash == null) {
            return;
        }

    }

    public void handOverKeysIfPossible(DataNode dataNode) {
    }
}
