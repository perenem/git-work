package com.example.pcRoom.service;

import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.UsersDto;
import com.example.pcRoom.entity.Menu;
import com.example.pcRoom.entity.Sell;
import com.example.pcRoom.entity.Users;
import com.example.pcRoom.repository.MenuRepository;
import com.example.pcRoom.repository.SellRepository;
import com.example.pcRoom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    MenuRepository menuRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    SellRepository sellRepository;
    @Autowired
    AdminService adminService;

    public List<MenuDto> showAllMenu() {
        List<Menu> menuList = menuRepository.findAll();
        List<MenuDto> menuDtoList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuDtoList.add(
                    MenuDto.fromMenuEntity(menu)
            );
        }
        return menuDtoList;
    } // 메뉴 전체를 가져오는 메소드

    public List<UsersDto> usersList() {
        List<UsersDto> usersDto = new ArrayList<>();
        return usersRepository.findAll()
                .stream()
                .map(x -> UsersDto.fromUserEntity(x))
                .toList();
    } // 모든 회원 정보 출력

    public void putMenuList(List<MenuDto> selectedMenus) {
        Map<String, Integer> menuFrequencyMap = new HashMap<>();

        for (MenuDto menuDto : selectedMenus) {
            String menuName = menuDto.getMenuName();
            menuFrequencyMap.put(menuName, menuFrequencyMap.getOrDefault(menuName, 0) + 1);
        } //메뉴이름 추출 , 빈도 계산

        for (Map.Entry<String, Integer> entry : menuFrequencyMap.entrySet()) {
            String menuName = entry.getKey();
            int frequency = entry.getValue();
            if (frequency != 0) { //빈도수가 0이 아닌 메뉴만
                Menu menu = menuRepository.findBymenuName(menuName); //메뉴이름에 맞는 메뉴데이터 가져옴
                Long menuId = menu.getMenuId(); // 메뉴이름에 맞는 메뉴아이디 가져옴
                Sell sell = new Sell(menuId, frequency);
                sellRepository.save(sell);
                adminService.minusSellAmountToMenuAmount(menuId, frequency);
            }
            // 메뉴이름 , 빈도 저장
        }

    } // 주문 들어온 메뉴들 db에 저장


//    public void chargedCoin(int money, String userId) {
//        Users users = (Users) usersRepository.findById().orElseThrow(() -> new RuntimeException("null"));
//        int currentMoney = users.getMoney();
//
//        switch (money) {
//            case 1:
//                currentMoney += 1000;
//                break;
//            case 2:
//                currentMoney += 2000;
//                break;
//            case 3:
//                currentMoney += 3000;
//                break;
//            case 4:
//                currentMoney += 5000;
//                break;
//            case 5:
//                currentMoney += 10000;
//                break;
//            case 6:
//                currentMoney += 20000;
//                break;
//        }
//        currentMoney = currentMoney+money;
//        users.setMoney(currentMoney);
//        usersRepository.save(users);
//    }


public void chargedCoin(Long userNo, int money){
        Users users = usersRepository.findByUserNo(userNo);
        if (users != null){
            int currentMoney = users.getMoney();
            users.setMoney(currentMoney + money);
            usersRepository.save(users);
        } else{
            System.out.printf("사용자가 없습니다.");
        }
    }
}
