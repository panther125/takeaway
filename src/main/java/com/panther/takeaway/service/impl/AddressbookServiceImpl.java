package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.entity.AddressBook;
import com.panther.takeaway.mapper.AddressBookMapper;
import com.panther.takeaway.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressbookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {
}
