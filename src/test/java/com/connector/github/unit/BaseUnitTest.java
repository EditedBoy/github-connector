package com.connector.github.unit;

import com.connector.github.BaseTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@TestInstance(Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
public class BaseUnitTest extends BaseTest {

}
