#include<iostream>
#include<string>

class Date {
    mutable bool cache_valid;
    mutable std::string cache;
    void compute_cache_value() const;                     //gyorstár feltöltése
    int d, m, y;
    static Date default_date;

    public:
    int day() const;
    int month() const;
    int year() const;
    int leapyear(int n);
    void init(int d, int m, int y);                 // kezdeti értékadás
    Date& add_year(int n);                           // n év hozzáadása
    Date& add_month(int n);                          // n hónap hozzáadása
    Date& add_day(int n);                            // n nap hozzáadása
    Date (int dd=0, int mm=0, int yy=0);
    std::string string_rep() const;                      //ábrázolás karakterlánccal
};

Date::Date(int dd, int mm, int yy){
    d = dd ? dd : default_date.d;
    m = mm ? mm : default_date.m;
    y = yy ? yy : default_date.y;
}

Date Date::default_date(16,12,1770);

inline Date &Date::add_year(int n)
{
    if (d==29 && m==2 && !leapyear(y+n)) {
        d = 1;
        m = 3;
    }
    y += n;
    return *this;
}

inline Date& Date::add_month(int n){ //nem másolja le, hanem a *this-t adja vissza.
    if(m+n>12){
        ++y;
        n-=12;
    }
    m += n;
    return *this;
}

inline Date& Date::add_day(int n){
    if(((m<8&&m%2)||(m>7&&!(m%2)))&&d+n>31){
        ++m;
        n-=31;
    }
    else if(((m<8&&!(m%2)&&m!=2)||(m>7&&m%2))&&d+n>30){
        ++m;
        n-=30;
    }
    else if(m==2&&!leapyear(y)&&n+d>28){
        ++m;
        n-=28;
    }
    else if(m%2&&n+d>29){
        ++m;
        n-=29;
    }
    d += n;
    return *this;
}

inline int Date::day() const{
    return d;
}

inline int Date::month() const{
    return m;
}

inline int Date::year() const{
    return y;
}

inline void
Date::init (int nap, int honap, int ev)
{
  y = ev;
  m = honap;
  d = nap;
}

inline int Date::leapyear(int n){
    if(n%400)
        return 1;
    else if (n%100)
        return 0;
    else if (n%4)
        return 1;
    else
        return 0;
}

void Date::compute_cache_value() const{
    std::cout<<"Type in cache text!\n";
    std::cin>>cache;
}

Date* hozzaad(Date* s){
    s->add_day(1).add_month(1).add_year(1);
    return s;
}

std::string Date::string_rep() const{

    if (!cache_valid) {

        compute_cache_value();
        cache_valid = true;
    }
    return cache;
}

int main(){
    const Date* xmas = new Date(25,12,1993);
    xmas->string_rep();
    std::cout<<xmas->year()<<"."<<xmas->month()<<"."<<xmas->day()<<"\n";
    delete xmas;
}