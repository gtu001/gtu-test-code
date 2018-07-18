package com.gtu.example.controller;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.example.springdata.dao_1.AddressCustomRepository;
import com.gtu.example.springdata.dao_1.AddressRepository;
import com.gtu.example.springdata.dao_1.EmployeePagingRepository;
import com.gtu.example.springdata.dao_1.EmployeeRepository;
import com.gtu.example.springdata.dao_1.EmployeeRepository.NamesOnly;
import com.gtu.example.springdata.dao_2.EmployeeJpaRepository;
import com.gtu.example.springdata.entity.Address;
import com.gtu.example.springdata.entity.Employee;

@Profile({ "spring-data" })

@RestController
@RequestMapping("/springdata/")
public class SpringDataController {

    private static final Logger log = LoggerFactory.getLogger(SpringDataController.class);

    private RandomAddressCreater addressCreater = new RandomAddressCreater();

    @Autowired
    private EmployeePagingRepository employeePagingRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressCustomRepository addressCustomRepository;

    @Autowired
    private EmployeeJpaRepository employeeJpaRepository;

    @RequestMapping(value = "/employee/create")
    public String employee_createOne() {
        String uuid = UUID.randomUUID().toString();
        Employee vo = new Employee("F_" + uuid, "L_" + uuid, "D_" + uuid);
        employeeRepository.save(vo);
        log.info(ReflectionToStringBuilder.toString(vo));
        return ReflectionToStringBuilder.toString(vo);
    }

    @RequestMapping(value = "/employee/findAll")
    public String employee_findAll() {
        Iterable<Employee> iter = employeeRepository.findAll();
        Stream<Employee> targetStream = StreamSupport.stream(iter.spliterator(), false);
        return targetStream.map((vo) -> ReflectionToStringBuilder.toString(vo))//
                .reduce("", (v1, v2) -> v1 += v2 + "<br/>");
    }

    @RequestMapping(value = "/employee/create_page")
    public String employee_createOne_page() {
        String uuid = UUID.randomUUID().toString();
        Employee vo = new Employee("F_" + uuid, "L_" + uuid, "D_" + uuid);
        employeePagingRepository.save(vo);
        log.info(ReflectionToStringBuilder.toString(vo));
        return ReflectionToStringBuilder.toString(vo);
    }

    @RequestMapping(value = "/employee/create2")
    public String employee_createOne2() {
        String uuid = UUID.randomUUID().toString();
        Employee vo = new Employee("F_" + uuid, "L_" + uuid, "D_" + uuid);
        employeeJpaRepository.save(vo);
        log.info(ReflectionToStringBuilder.toString(vo));
        return ReflectionToStringBuilder.toString(vo);
    }

    @RequestMapping(value = "/employee/findAll2")
    public String employee_findAll2() {
        Iterable<Employee> iter = employeeJpaRepository.findAll();
        Stream<Employee> targetStream = StreamSupport.stream(iter.spliterator(), false);
        return targetStream.map((vo) -> ReflectionToStringBuilder.toString(vo))//
                .reduce("", (v1, v2) -> v1 += v2 + "<br/>");
    }

    @RequestMapping(value = "/employee/findAll_nameOnly")
    public String employee_findAll_nameOnly() {
        Iterable<NamesOnly> iter = employeeRepository.findAll_nameOnly();
        Stream<NamesOnly> targetStream = StreamSupport.stream(iter.spliterator(), false);
        return targetStream.map((vo) -> vo.getFullName())//
                .reduce("", (v1, v2) -> v1 += v2 + "<br/>");
    }

    @RequestMapping(value = "/address/create")
    public String address_createOne() {
        Address d1 = addressCreater.getNewAddress();
        addressRepository.save(d1);
        log.info(ReflectionToStringBuilder.toString(d1));
        return ReflectionToStringBuilder.toString(d1);
    }

    @RequestMapping(value = "/address/create_custom")
    public String address_createOne_custom() {
        Address d1 = addressCreater.getNewAddress();
        addressCustomRepository.persist(d1);
        log.info(ReflectionToStringBuilder.toString(d1));
        return ReflectionToStringBuilder.toString(d1);
    }

    @RequestMapping(value = "/address/findAll_withRownum")
    public String address_findAll_withRownum() {
        Iterable<Address> iter = addressCustomRepository.findAllWithRownum();
        return StreamSupport.stream(iter.spliterator(), false)//
                .map((vo) -> ReflectionToStringBuilder.toString(vo))//
                .reduce("", (v1, v2) -> v1 += v2 + "<br/>");
    }

    private static class RandomAddressCreater {
        String[] citys = new String[] { "名古屋市", "豊橋市", "岡崎市", "一宮市", "半田市", "春日井市", "豊川市", "津島市", "碧南市", "刈谷市", "豊田市", "安城市", "西尾市", "蒲郡市", "犬山市", "常滑市", "江南市", "小牧市", "東海市", "大府市", };
        String[] roads = new String[] { "Hideaki HAYASHI", "Makio HAYAKAWA", "Yasuyuki BABA", "Risonanakameguro Bldg,", "Ryuta EGUCHI", "Shinichi MATSUDA", "Youji KOMAI", "Chikako FUJITA",
                "Masatsugu IMAOKA", "Tadashi OBAYASHI", "Masahiro ENOMOTO", "Yoichi TAMEGAI", "Hiroshi KOYAMA", "Hideki KURITA", "Kazumasa NODA", "Kazuro NAGASE", "Masami KOITA", "Takaki TAMAMIZU",
                "Nobuhiro ISHIKAWA", "Murakami Co., Ltd.", "Hirotaka MURAKAMI", "Keiithi NAGASE", "Hiroshi KUBO", "NBC Meshtec Inc.", "Hiroshi OHTAKA", "Masayuki ITAGAKI", "Kano SHIMIZU",
                "Tetsuro EMORI", "Ryoji HATTORI", "Akira SAWADA", "Tetsuo HIRAGURI", "SERIA CORPORATION", "Yutaka SHINODA", "Yuichi TORII", "Hideo WATANABE", "Hiroyuki DEWAKE", };

        private String getCity() {
            return citys[new Random().nextInt(citys.length)];
        }

        private String getRoad() {
            return roads[new Random().nextInt(roads.length)];
        }

        private Address getNewAddress() {
            Address ad = new Address(getCity(), getRoad());
            return ad;
        }
    }
}
