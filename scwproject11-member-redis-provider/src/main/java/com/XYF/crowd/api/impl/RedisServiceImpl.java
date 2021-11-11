package com.XYF.crowd.api.impl;

import com.XYF.crowd.api.RedisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @username 熊一飞
 */

@Service
@Transactional(readOnly = true)
public class RedisServiceImpl implements RedisService {
}
